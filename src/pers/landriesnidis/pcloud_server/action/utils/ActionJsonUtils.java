package pers.landriesnidis.pcloud_server.action.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionJsonUtils {
	
	//显示信息
	private static Date date = new Date(); 
	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	public static void showInfo(String action, boolean state, String username,String content){
		String time = format.format(date);
		String strFormat = "Time:%s\nAction:%s\nState:%b\nUser:%s\n%s";
		String text = String.format(strFormat, time, action, state, username, content);
		System.out.println("\n==================");
		System.out.println(text);
		System.out.println("==================");
	}
}
