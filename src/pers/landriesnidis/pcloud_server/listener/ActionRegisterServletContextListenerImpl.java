package pers.landriesnidis.pcloud_server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import pers.landriesnidis.pcloud_server.action.ActionManager;
import pers.landriesnidis.pcloud_server.action.FolderMenuAction;
import pers.landriesnidis.pcloud_server.action.UserLoginAction;
import pers.landriesnidis.pcloud_server.action.UserLogoutAction;
import pers.landriesnidis.pcloud_server.action.UserRegisterAction;
import pers.landriesnidis.pcloud_server.action.UserUploadAction;

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
		
		//用户上传
		manager.registerAction(POST, UserUploadAction.class);
	}
}
