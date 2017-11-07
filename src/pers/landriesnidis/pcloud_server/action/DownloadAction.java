package pers.landriesnidis.pcloud_server.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import pers.landriesnidis.pcloud_server.ConfigManager;
import pers.landriesnidis.pcloud_server.action.utils.ActionJson;
import pers.landriesnidis.pcloud_server.database.SqlDataProvider;

public class DownloadAction extends BaseServletAction {

	public DownloadAction() {
		super("Download");
	}

	@Override
	public void execute(HttpServletRequest request,
			HttpServletResponse response, HttpServlet httpServlet) {
		
		try {
			//构建Json字符串工具类
			ActionJson aJson = new ActionJson(getActionName());
			//读取请求参数
			String token = getRequestParameter("token");
			String filename = getRequestParameter("filename");		//仅用作标记下载文件的文件名
			int fileid = Integer.parseInt(getRequestParameter("fileid"));
			
			//若未指定文件名则随机生成字符串
			if(filename==null)filename = UUID.randomUUID().toString();
			
			try {
				// 数据库操作 - 通过文件Id获取文件的路径(会触发SQLException)
				String path = SqlDataProvider.GetFilePathById(token, fileid);
				//文件的真实路径
				String realPath = ConfigManager.getManager().FILE_SAVE_PATH + path;
				//文件对象
				File file = new File(realPath);
				//判断文件是否存在
				if (file.exists()) {		
					// 如果文件存在
					// 设置文件MIME类型
					response.setContentType("application/octet-stream");
					// 设置Content-Disposition
					response.setHeader("Content-Disposition", 
							"attachment;filename=" + new String(filename.getBytes("UTF-8"),"ISO-8859-1"));
					// 读取目标文件，通过response将目标文件写到客户端
					// 获取目标文件的绝对路径
					response.setContentLength((int)new File(realPath).length());
					// 读取文件
					OutputStream out = response.getOutputStream();
					InputStream in = new FileInputStream(file);
					byte[] buffer = new byte[1024];
			        int i = -1;
			        while ((i = in.read(buffer)) != -1) {
			         out.write(buffer, 0, i);
			        }
			        in.close();
					out.close();
				}else{
					//如果文件不存在
					PrintWriter out = response.getWriter();
					aJson.setFlag(false).setMessage("文件不存在");
					out.println(aJson.toString());
					out.flush();
					out.close();
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
				PrintWriter out = response.getWriter();
				out.println(aJson.toString());
				out.flush();
				out.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
