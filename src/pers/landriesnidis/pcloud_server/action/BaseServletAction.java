package pers.landriesnidis.pcloud_server.action;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.landriesnidis.pcloud_server.database.ErrorCode;

public abstract class BaseServletAction {
	
	private String strActionName;
	private String encode = "UTF-8";
	private String method;
	private HttpServletRequest request;
	
	public BaseServletAction(String actionName) {
		strActionName = actionName;
	}

	/**
	 * @param actionName	
	 * @param encode		字符编码(默认为UTF-8)
	 */
	public BaseServletAction(String actionName, String encode) {
		strActionName = actionName;
		this.encode = encode;
	}
	
	/**
	 * 获取Action名称
	 * @return
	 */
	public String getActionName() {
		return strActionName;
	}
	
	/**
	 * 请求委托
	 * @param request
	 * @param response
	 * @param httpServlet
	 */
	public void entrust(HttpServletRequest request, HttpServletResponse response, HttpServlet httpServlet){
		method = request.getMethod();
		this.request = request;
		response.setCharacterEncoding(encode);
		
		//设置POST请求的字符编码
		if(method.equals("POST")){
			try {
				request.setCharacterEncoding(encode);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		//设置Content-Type
		response.setHeader("Content-Type", "text/html;charset=" + encode);
		
		execute(request, response, httpServlet);
	}
	
	/**
	 * 具体执行方法
	 * @param request
	 * @param response
	 * @param httpServlet
	 */
	protected abstract void execute(HttpServletRequest request, HttpServletResponse response, HttpServlet httpServlet);

	/**
	 * 获取请求参数
	 * @param key
	 * @return
	 */
	public String getRequestParameter(String key){
		//直接读取请求的结果
		String value = request.getParameter(key);
		//判断请求的方法
		if(method.equals("GET")){
			try {
				value = new String(value.getBytes("ISO8859-1"),encode);
				return value;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	 * 从SQLException对象中获取提示信息
	 * @param e	逻辑错误返回提示信息，其他错误返回null
	 * @return
	 */
	protected String getPromptFromSQLException(SQLException e){
		if(e.getErrorCode()==1644){
			String errorcode = e.getMessage();
			String message = null;
			try {
				//利用反射机制
				message = (String) ErrorCode.class.getField(errorcode).get(errorcode);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				//没有定义对应的ERROR CODE
				//e1.printStackTrace();
				message = "错误：" + errorcode;
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			return message;
		}else{
			return null;
		}
	}
	
	/**
	 * 设置字符编码
	 * @param encode	字符编码(默认为UTF-8)
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}
	
	/**
	 * 获取字符编码
	 * @return	字符编码
	 */
	public String getEncode() {
		return encode;
	}
}
