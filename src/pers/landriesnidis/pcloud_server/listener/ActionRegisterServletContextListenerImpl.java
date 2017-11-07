package pers.landriesnidis.pcloud_server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import pers.landriesnidis.pcloud_server.action.ActionManager;
import pers.landriesnidis.pcloud_server.action.CopyFileAction;
import pers.landriesnidis.pcloud_server.action.CreateFolderAction;
import pers.landriesnidis.pcloud_server.action.DeleteFileAction;
import pers.landriesnidis.pcloud_server.action.DownloadAction;
import pers.landriesnidis.pcloud_server.action.FolderMenuAction;
import pers.landriesnidis.pcloud_server.action.MoveFileAction;
import pers.landriesnidis.pcloud_server.action.RenameFileAction;
import pers.landriesnidis.pcloud_server.action.UserLoginAction;
import pers.landriesnidis.pcloud_server.action.UserLogoutAction;
import pers.landriesnidis.pcloud_server.action.UserRegisterAction;
import pers.landriesnidis.pcloud_server.action.UploadAction;

public class ActionRegisterServletContextListenerImpl  implements ServletContextListener{

	public static final String POST = "POST";
	public static final String GET  = "GET";
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ActionManager manager = ActionManager.getInstance();
		
		//用户登录
		manager.registerAction(GET , UserLoginAction.class);
		manager.registerAction(POST, UserLoginAction.class);
		
		//用户登出
		manager.registerAction(GET , UserLogoutAction.class);
		manager.registerAction(POST, UserLogoutAction.class);
		
		//用户注册
		manager.registerAction(GET , UserRegisterAction.class);
		manager.registerAction(POST, UserRegisterAction.class);
		
		//获取文件夹目录
		manager.registerAction(GET , FolderMenuAction.class);
		manager.registerAction(POST, FolderMenuAction.class);
		
		//下载
		manager.registerAction(GET , DownloadAction.class);
		manager.registerAction(POST, DownloadAction.class);
		
		//上传
		manager.registerAction(POST, UploadAction.class);
		
		//文件拷贝
		manager.registerAction(GET , CopyFileAction.class);
		manager.registerAction(POST, CopyFileAction.class);
		
		//创建新文件夹
		manager.registerAction(GET , CreateFolderAction.class);
		manager.registerAction(POST, CreateFolderAction.class);
		
		//文件重命名
		manager.registerAction(GET , RenameFileAction.class);
		manager.registerAction(POST, RenameFileAction.class);
		
		//删除文件
		manager.registerAction(GET , DeleteFileAction.class);
		manager.registerAction(POST, DeleteFileAction.class);
		
		//移动文件
		manager.registerAction(GET , MoveFileAction.class);
		manager.registerAction(POST, MoveFileAction.class);
		
		//文件夹重命名
		
		//文件夹移动
		
		//文件夹删除
	}
}
