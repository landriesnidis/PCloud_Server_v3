package org.nisita.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

class ConnectionPool {

	private static final int MIN_POOL_SIZE = 5;
	private static final int MAX_POOL_SIZE = 20;
	private static final int INCREAMENT_SIZE = 5;
	
	/**
	 * 当前连接池大小
	 */
	private int mSize = 0;
	
	/**
	 * 连接池
	 */
	private LinkedList<Connection> mConnPool = null;
	
	private ConnectionPool() throws ClassNotFoundException, SQLException {
		mConnPool = new LinkedList<Connection>();
		Class.forName(Config.getDriver());
		createNewConnections(MIN_POOL_SIZE);
	}
	
	/**
	 * 创建新连接
	 * @param size 连接数
	 * @throws SQLException 
	 */
	private void createNewConnections(int size) throws SQLException {
		//如果目标连接数大于最大连接数则直接返回
		if (size + mConnPool.size() > MAX_POOL_SIZE) {
			return;
		}
		for (int i = 0; i < size; i++) {
			Connection conn = DriverManager.getConnection(
					Config.getUrl(), Config.getUser(), Config.getPassword());
			mConnPool.addLast(conn);
			mSize++;
		}
	}
	
	/**
	 * 释放链接，将连接还给连接池
	 */
	public synchronized void release(Connection conn) {
		mConnPool.addLast(conn);
		this.notifyAll();
	}
	
	/**
	 * 清空连接池
	 */
	public synchronized void clear() {
		Iterator<Connection> iterator = mConnPool.iterator();
		while (iterator.hasNext()) {
			Connection action = iterator.next();
			try {
				action.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			iterator.remove();
			mSize--;
		}
	}
	
	private static ConnectionPool mPool = null;
	public static synchronized ConnectionPool getInstance() {
		if (mPool == null) {
			try {
				mPool = new ConnectionPool();
			} catch (ClassNotFoundException e) {
				System.err.println("jdbc driver error");
				e.printStackTrace();
			} catch (SQLException e) {
				System.err.println("jdbc url login error");
				e.printStackTrace();
			}
		}
		return mPool;
	}
	
	public synchronized Connection getConnection() {
		Connection conn = null;
		while (mConnPool.size() == 0) {//size = 0 说明没有连接
			if (mSize >= MAX_POOL_SIZE) {
				try {
					this.wait();
				} catch (InterruptedException e) {}
			} else {
				//扩大连接池
				try {
					createNewConnections(INCREAMENT_SIZE);
				} catch (SQLException e) {
					System.err.println("创建新连接失败");
					e.printStackTrace();
				}
			}
		}
		//从已有连接中拿到第一个返回
		conn = mConnPool.getFirst();
		return conn;
	}
}
