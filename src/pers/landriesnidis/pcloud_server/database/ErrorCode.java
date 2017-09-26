package pers.landriesnidis.pcloud_server.database;

public class ErrorCode {

	public static final String IDENTITY_INVALID = "用户身份已失效，请重新登录。";
	public static final String LOGIN_TIMEOUT = "用户登录超时，请重新登录。";
	public static final String UNKNOW_FOLDER = "文件夹不存在。";
	public static final String HAVE_NOT_UPLOADED = "您还没有上传的文件。";
	public static final String FILE_ALREADY_EXISTS = "该文件夹中已有同名文件存在。";
	public static final String FOLDER_ALREADY_EXISTS = "该文件夹中已有同名文件夹存在。";
	public static final String AUTHENTICATION_FAILED = "用户名或密码不正确。";
	public static final String ACCOUNT_OCCUPIED = "该用户名已经被占用。";

}
