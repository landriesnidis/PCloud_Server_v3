package pers.landriesnidis.pcloud_server.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nisita.mysql.DBAction;

public class SqlQueryHelper {

	public static void main(String[] args) {
		try {
			String token;
			JSONArray list;
			boolean result;
			
			token = UserLogin("admin","123456");
			System.out.println("用户登录：" + token);
			
//			result = UserLogout(token);
//			System.out.println("用户登出：" + result);
			
//			token = UserRegister("李贵杰","123456");
//			System.out.println("注册用户：" + token);
			
			list = GetFolderListByFolderId(token, 1);
			System.out.println("子文件夹：" + list);
			
			list = GetFileListByFolderId(token,1);
			System.out.println("文件列表：" + list);
			
			result = UserRenameFile(token,1,1,"腾讯QQ.exe");
			System.out.println("重命名：" + result);
			
		} catch (SQLException e) {
			if(e.getErrorCode()==1644){
				System.out.println("错误信息：" + e.getMessage());
			}else{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * TODO 用户登录
	 * @param account	用户名
	 * @param password	密码
	 * @return			token
	 * @throws SQLException
	 */
	public static String UserLogin(final String account, final String password) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT UserLogin(?,?) As token;",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, account);
				pstmt.setString(2, password);
			}
		});
		return (String) lm.get(0).get("token");
	}
	
	/**
	 * TODO 用户登出
	 * @param token	
	 * @return
	 * @throws SQLException
	 */
	public static boolean UserLogout(final String token) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT UserLogout(?) As result;",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
			}
		});
		int result = (Integer) lm.get(0).get("result");
		return result==1;
	}
	
	/**
	 * TODO 注册用户
	 * @param account	用户名
	 * @param password	密码
	 * @return			token
	 * @throws SQLException
	 */
	public static String UserRegister(final String account, final String password) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT UserRegister(?,?) As token;",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, account);
				pstmt.setString(2, password);
			}
		});
		return (String) lm.get(0).get("token");
	}
	
	/**
	 * TODO 通过文件夹id获取子文件夹列表
	 * @param token
	 * @param folderid
	 * @return
	 * @throws SQLException
	 */
	public static JSONArray GetFolderListByFolderId(final String token ,final int folderid) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("CALL GetFolderListByFolderId(?,?)",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setInt(2, folderid);
			}
		});
		
		// 将List<HashMap<String,Object>>格式的数据转换为JSONArray格式
		JSONArray array = new JSONArray();
		String[] fields = new String[] { "id", "foldername"};
		for (int i = 0; i < lm.size(); ++i) {
			JSONObject json = new JSONObject();
			for (int j = 0; j < fields.length; ++j) {
				try {
					json.put(fields[j], lm.get(i).get(fields[j]));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			array.put(json);
		}
		
		return array;
	}
	
	/**
	 * TODO 通过文件夹id获取文件列表
	 * @param token
	 * @param folderid
	 * @return
	 * @throws SQLException
	 */
	public static JSONArray GetFileListByFolderId(final String token ,final int folderid) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("CALL GetFileListByFolderId(?,?)",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setInt(2, folderid);
			}
		});
		
		//将List<HashMap<String,Object>>格式的数据转换为JSONArray格式 
		JSONArray array = new JSONArray();
		String[] fields = new String[]{"id","md5","tempcode","filename","size"};
		for(int i=0;i<lm.size();++i){
			JSONObject json = new JSONObject();
			for(int j=0;j<fields.length;++j){
				try {
					json.put(fields[j], lm.get(i).get(fields[j]));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			array.put(json);
		}
		
		return array;
	}
	
	/**
	 * TODO 通过文件夹id获取文件夹内所有的子文件夹和文件
	 * @param token
	 * @param folderid
	 * @return
	 * @throws SQLException
	 */
	public static JSONObject GetAllByFolderId(final String token ,final int folderid) throws SQLException{
		JSONObject json = new JSONObject();
		try {
			json.put("folder", GetFolderListByFolderId(token, folderid));
			json.put("file", GetFileListByFolderId(token, folderid));
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String GetFilePathById(final String token ,final int fileid) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT GetFilePathById(?,?) As result;",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setInt(2, fileid);
			}
		});
		return (String) lm.get(0).get("result");
	}
	
	/**
	 * TODO 文件重命名
	 * @param token
	 * @param folderid
	 * @param fileid
	 * @param newname
	 * @return
	 * @throws SQLException
	 */
	public static boolean UserRenameFile(final String token ,final int folderid ,final int fileid, final String newname) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT UserRenameFile(?,?,?,?) As result;",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setInt(2, folderid);
				pstmt.setInt(3, fileid);
				pstmt.setString(4, newname);
			}
		});
		int result = (Integer) lm.get(0).get("result");
		return result==1;
	}
	
	/**
	 * TODO 查询文件是否存在
	 * @param md5
	 * @return
	 * @throws SQLException
	 */
	public static boolean CheckFileExists(final String md5) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT CheckFileExists(?) As result;",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, md5);
			}
		});
		int result = (Integer) lm.get(0).get("result");
		return result!=0;
	}
	
	/**
	 * TODO 为用户添加所属的文件记录
	 * @param token
	 * @param filemd5
	 * @param filename
	 * @param folderid
	 * @throws SQLException
	 */
	public static void UserAddFile(final String token ,final String filemd5, final String filename, final int folderid) throws SQLException{
		DBAction action = new DBAction();
		action.exeUpdate("CALL UserAddFile(?,?,?,?);",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setString(2, filemd5);
				pstmt.setString(3, filename);
				pstmt.setInt(4, folderid);
			}
		});
	}
	
	/**
	 * TODO 文件上传记录
	 * @param token
	 * @param filemd5
	 * @param filesize
	 * @throws SQLException
	 */
	public static void FileUpload(final String token, final String filepath, final String filemd5, final long filesize) throws SQLException{
		DBAction action = new DBAction();
		action.exeUpdate("CALL FileUpload(?,?,?,?);",new DBAction.ParameterSetter() {
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setString(2, filemd5);
				pstmt.setString(3, filemd5);
				pstmt.setLong(4, filesize);
				
			}
		});
	}
	
	public static boolean CopyFile(final String token,final String folder1,final String folder2,final String fileid) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT UserCopyFile(?,?,?,?) AS result;", new DBAction.ParameterSetter() {
			
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setString(2, folder1);
				pstmt.setString(3, folder2);
				pstmt.setString(4, fileid);
			}
		});
		int result = (Integer)lm.get(0).get("result");
		return result==1;
	}
	
	public static int CreateFolder(final String token,final String folderid,final String newfoldername) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT UserCreateFolder(?,?,?) AS result;", new DBAction.ParameterSetter() {
			
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setString(2, folderid);
				pstmt.setString(3, newfoldername);
			}
		});
		return (Integer)lm.get(0).get("result");
	}
	
	public static boolean RenameFile(final String token,final int folderid,final int fileid,final String namefilename) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT UserRenameFile(?,?,?,?) AS result;", new DBAction.ParameterSetter() {
			
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setInt(2, folderid);
				pstmt.setInt(3, fileid);
				pstmt.setString(4, namefilename);
			}
		});
		int result = (Integer)lm.get(0).get("result");
		return result==1;
	}
	
	public static boolean DeleteFile(final String token,final int folderid,final int fileid) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT UserDeleteFile(?,?,?) AS result;", new DBAction.ParameterSetter() {
			
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setInt(2, folderid);
				pstmt.setInt(3, fileid);
			}
		});
		int result = (Integer)lm.get(0).get("result");
		return result==1;
	}
	
	public static boolean MoveFile(final String token,final int folder1id,final int folder2id,final int fileid) throws SQLException{
		DBAction action = new DBAction();
		List<HashMap<String,Object>> lm = action.exeQuery("SELECT UserMoveFile(?,?,?,?) AS result;", new DBAction.ParameterSetter() {
			
			@Override
			public void setParameter(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, token);
				pstmt.setInt(2, folder1id);
				pstmt.setInt(3, folder2id);
				pstmt.setInt(4, fileid);
			}
		});
		int result = (Integer)lm.get(0).get("result");
		return result==1;
	}
	
	
	
}
