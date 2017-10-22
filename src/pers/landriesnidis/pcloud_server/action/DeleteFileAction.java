package pers.landriesnidis.pcloud_server.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.landriesnidis.pcloud_server.action.utils.ActionJson;
import pers.landriesnidis.pcloud_server.database.SqlQueryHelper;

public class DeleteFileAction extends BaseServletAction {

	public DeleteFileAction() {
		super("DeleteFile");
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
			String token = getRequestParameter("token");
			String folderid = getRequestParameter("folderid");
			String fileid = getRequestParameter("fileid");
			try {
				// 数据库操作 - 删除文件(会触发SQLException)
				boolean result= SqlQueryHelper.DeleteFile(token, Integer.parseInt(folderid), Integer.parseInt(fileid));

				// 向ActionJson对象赋值
				// 标记操作成功
				aJson.setFlag(result);
				if(!result){
					aJson.setContent("删除失败，文件不存在.");
				}

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