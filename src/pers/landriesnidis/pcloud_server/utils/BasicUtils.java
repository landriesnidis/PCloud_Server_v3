package pers.landriesnidis.pcloud_server.utils;

import java.io.File;

public class BasicUtils {
	
//	public static void main(String[] args) {
//		System.out.println(fileSize(new File("G:\\视频\\独裁者.mkv")));
//	}
	
	/**
	 * 估算文件大小
	 * @param file 文件对象
	 * @return
	 */
	public static String fileSize(File file){
		double size = file.length();
		String[] arr = new String[]{"B","KB","MB","GB","TB"};
		int i = 0;
		while(true){
			if(size>1000 && i<arr.length){
				size = size/1000;
				i++;
			}else{
				return String.format("%.2f %s", size,arr[i]);
			}
		}
	}
}
