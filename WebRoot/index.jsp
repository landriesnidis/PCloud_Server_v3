<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Download Client</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  
  <body>
  	【使用说明】<br><br>
  	解压后文件夹后用记事本打开目录下文件host.conf，将服务器的IP地址写入文件，运行客户端程序即可。<br>
  	<br><br>
           客户端：Windows 32位/64位 <a href="PCloud-client-1.0-windows-x86_amd64.zip">下载</a> <br>
  </body>
</html>
