package pers.landriesnidis.pcloud_server.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import pers.landriesnidis.pcloud_server.ConfigManager;
import pers.landriesnidis.pcloud_server.action.utils.ActionJson;
import pers.landriesnidis.pcloud_server.database.SqlDataProvider;
import pers.landriesnidis.pcloud_server.utils.MD5;

public class UploadAction extends BaseServletAction {

	public UploadAction() throws IOException {
		super("Upload");
	}

	@Override
	protected void execute(HttpServletRequest request,
			HttpServletResponse response, HttpServlet httpServlet) {
		
		try {
			//输出对象
			PrintWriter writer = response.getWriter();
			//构建Json字符串工具类
			ActionJson aJson = new ActionJson(getActionName());
			//读取请求参数
			final String token = getRequestParameter("token");
			String filemd5 = getRequestParameter("filemd5");
			String filename = getRequestParameter("filename");
			int folderid = Integer.parseInt(getRequestParameter("folderid"));
			
			//查询服务器中是否已有该文件
			if(!SqlDataProvider.CheckFileExists(filemd5)){
				//如果服务器中没有相同文件则 接收上传的文件
				receiveFile(request, new OnFileReceiveListener() {
					@Override
					public void onFinished(String md5,String filename, long size) throws SQLException {
						SqlDataProvider.FileUpload(token, filename, md5, size);
					}
				});
			}
			
			//为用户添加所属的新文件记录
			try {
				SqlDataProvider.UserAddFile(token, filemd5, filename, folderid);
			} catch (SQLException e) {	//这里的SQLException会报自定义的异常
				// 标记操作失败
				aJson.setFlag(false);
				
				//获取逻辑错误的提示信息
				String info = getPromptFromSQLException(e);
				System.err.println("数据库自定义函数查询错误：" + info);
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	private void receiveFile(HttpServletRequest request, OnFileReceiveListener listener) throws Exception{
		
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// 临时文件夹路径
		String repositoryPath = ConfigManager.getManager().FILE_TEMP_PATH;
		// 设定临时文件夹为repositoryPath
		File fileTempFolder = new File(repositoryPath);
		if(!fileTempFolder.exists())fileTempFolder.mkdirs();
		factory.setRepository(fileTempFolder);

		// 设定上传文件的阈值，如果上传文件大于1M，就可能在repository
		// 所代 表的文件夹中产生临时文件，否则直接在内存中进行处理
		factory.setSizeThreshold(1024 * 1024);

		// 创建一个ServletFileUpload对象
		ServletFileUpload uploader = new ServletFileUpload(factory);

			// 调用uploader中的parseRequest方法，可以获得请求中的相关内容，
			// 即一个FileItem类型的ArrayList。FileItem是在
			// org.apache.commons.fileupload中定义的，它可以代表一个文件，
			// 也可以代表一个普通的form field
			ArrayList<FileItem> list = (ArrayList<FileItem>) uploader.parseRequest(request);
			for (FileItem fileItem : list){
				if (!fileItem.isFormField()){ // 如果是文件
					String fileName = fileItem.getName() + UUID.randomUUID();
					// 将其中包含的内容写到指定目录下名为fileName的文件中
					File file = new File(ConfigManager.getManager().FILE_SAVE_PATH + File.separator + fileName);
					file.createNewFile();
//					System.out.println(file.getPath());
					fileItem.write(file);
					
					listener.onFinished(MD5.getMD5CodeByFile(file),fileName,file.length());
					break;//一次请求只接受一个文件
				}
			}
	}
	
	interface OnFileReceiveListener{
		void onFinished(String md5,String fileName,long size) throws SQLException;
	}

}
