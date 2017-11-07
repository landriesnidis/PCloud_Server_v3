package org.nisita.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBAction {

	private Connection mConn = null;
	
	private ConnectionPool mPool = null;
	
	/**
	 * 当前是否在执行事务
	 */
	private boolean bTransaction = false;
	
	public DBAction() {
		mPool = ConnectionPool.getInstance();
	}
	
	/**
	 * 获取结果集列名
	 * @param rsmd
	 * @return
	 */
	private List<String> getColumnList(ResultSet rs) {
		ResultSetMetaData rsmd = null;
		List<String> listColumn = null;
		try {
			rsmd = rs.getMetaData();
		
			listColumn = new ArrayList<String>();
			for (int i = 1, count = rsmd.getColumnCount(); i <= count; i++) {
				listColumn.add(rsmd.getColumnLabel(i));
			}
		} catch (SQLException e) {
			System.err.println("获取列名出错");
			e.printStackTrace();
			return null;
		}
		return listColumn;
	}
	
	private Connection getConnection() throws SQLException {
		Connection conn = mPool.getConnection();
		if (bTransaction) {
			conn.setAutoCommit(false);
		}
		return conn;
	}
	
	/**
	 * 无参数查询
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, Object>> exeQuery(String sql) throws SQLException {
		if (!bTransaction || mConn == null){
			mConn = getConnection();
		}
		Statement stmt = mConn.createStatement();
		
		ResultSet rs = stmt.executeQuery(sql);
		
		List<HashMap<String, Object>> resultSet = getResultSet(rs);
		
		stmt.close();
		close();
		return resultSet;
	}
	
	public interface ParameterSetter{
		void setParameter(PreparedStatement pstmt) throws SQLException;
	}
	
	/**
	 * 将ResultSet转换成ArrayList<HashMap>
	 * @param rs
	 * @return
	 */
	private List<HashMap<String, Object>> getResultSet(ResultSet rs) {
		List<String> listColumn = getColumnList(rs);
		List<HashMap<String,Object>> resultSet = null;
		try {
			resultSet = new ArrayList<HashMap<String,Object>>();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int i = 0, size = listColumn.size(); i < size; i++) {
					String key = listColumn.get(i);
					Object value = rs.getObject(i + 1);
					map.put(key, value);
				}
				resultSet.add(map);
			}
		} catch (SQLException e) {
			System.err.println("获取结果集失败");
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * 无参数更新
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private int exeUpdate(String sql) throws SQLException {
		if (!bTransaction || mConn == null){
			mConn = getConnection();
		}
		Statement stmt = mConn.createStatement();
		
		int col = stmt.executeUpdate(sql);
		
		stmt.close();
		close();
		return col;
	}
	

	/**
	 * 预编译查询
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 * @throws QueryException 未查到内容
	 */
	public List<HashMap<String, Object>> exeQuery(String sql, 
			ParameterSetter paramSetter) throws SQLException {
		if (paramSetter == null) {
			return exeQuery(sql);
		}
		
		if (!bTransaction || mConn == null){
			mConn = getConnection();
		}
		PreparedStatement ps = mConn.prepareStatement(sql);
		//添加参数
		paramSetter.setParameter(ps);
		
		ResultSet rs = ps.executeQuery();
		
		List<HashMap<String, Object>> resultSet = getResultSet(rs);
		
		ps.close();
		close();

		return resultSet;
	}
	
	/**
	 * 预编译更新
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 */
	public int exeUpdate(String sql, ParameterSetter paramSetter) throws SQLException {
		if (paramSetter == null) {
			return exeUpdate(sql);
		}
		
		if (!bTransaction || mConn == null){
			mConn = getConnection();
		}
		PreparedStatement ps = mConn.prepareStatement(sql);
		//添加参数
		paramSetter.setParameter(ps);
		
		int col = ps.executeUpdate();
		
		ps.close();
		close();
		return col;
	}
	
	public void beginTransaction() throws SQLException {
		bTransaction = true;
		mConn = mPool.getConnection();
		mConn.setAutoCommit(false);
	}
	
	public void commit() throws SQLException {
		mConn.commit();
		mConn.setAutoCommit(true);
		bTransaction = false;
		close();
	}
	
	public void rollback() {
		try {
			mConn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		bTransaction = false;
		close();
	}
	
	private void close() {
		if (bTransaction) {
			return;
		}
		mPool.release(mConn);
	}
}
