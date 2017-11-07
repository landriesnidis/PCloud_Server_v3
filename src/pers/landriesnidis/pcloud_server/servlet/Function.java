package pers.landriesnidis.pcloud_server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.landriesnidis.pcloud_server.action.ActionManager;
import pers.landriesnidis.pcloud_server.action.BaseServletAction;
import pers.landriesnidis.pcloud_server.action.exception.ActionUnregisteredException;

public class Function extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static ActionManager manager = ActionManager.getInstance();
	

	public Function() {
		super();
	}
	
	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取请求的Action
		String actionName = request.getParameter("Action");
		//创建处理对应事件的Action对象
		BaseServletAction bsa = null;
		try {
			bsa = manager.createActionByName(request.getMethod(),actionName);
		} catch (ActionUnregisteredException e) {
			System.err.println(e.getMessage());
//			e.printStackTrace();
			return;
		}
		// 委托于相应Action处理对象
		bsa.entrust(request, response, this);
		bsa = null;
		// 显示请求信息
		System.out.println(String.format("接收到[%s]请求，请求方式为[%s]，请求Action为[%s]", request.getProtocol(),request.getMethod(),actionName));

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void init() throws ServletException {
		
	}

}
