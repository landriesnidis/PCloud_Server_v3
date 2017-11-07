package pers.landriesnidis.pcloud_server.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.landriesnidis.pcloud_server.action.utils.ActionJson;
import pers.landriesnidis.pcloud_server.database.SqlDataProvider;

public class UserRegisterAction extends BaseServletAction {

	public UserRegisterAction() {
		super("UserRegister");
	}

	@Override
	public void execute(HttpServletRequest request,
			HttpServletResponse response, HttpServlet httpServlet) {
		
		try {
			//输出对象
			PrintWriter writer = response.getWriter();
			//构建Json字符串工具类
			ActionJson aJson = new ActionJson(getActionName());
			//读取请求参数
			String username = getRequestParameter("username");
			String password = getRequestParameter("password");
			try {
				// 数据库操作 - 获取用户的token(会触发SQLException)
				String token = SqlDataProvider.UserRegister(username, password);

				// 向ActionJson对象赋值
				// 标记操作成功
				aJson.setFlag(true);
				// 设置内容为token
				aJson.setContent(token);

			} catch (SQLException e) {
				// 标记操作失败
				aJson.setFlag(false);
				
				//获取逻辑错误的提示信息
				String info = getPromptFromSQLException(e);
				//判断是否是逻辑错误，设置相应提示信息
				if(info!=null){
					aJson.setMessage(info);
				}else{
					aJson.setMessage("数据库未知错误.");
					//显示错误信息
					e.printStackTrace();
				}
			} finally {
				// 返回请求数据并关闭连接
				writer.println(aJson.toString());
				//关闭连接
				writer.flush();
				writer.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


}
