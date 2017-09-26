package pers.landriesnidis.pcloud_server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class MD5 {

	/**
	 * 获取文件的MD5
	 * @param file	File文件对象
	 * @return
	 */
	public static String getMD5CodeByFile(File file){
		try{
			FileInputStream fis = new FileInputStream(file);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buf = new byte[1024];
			int length = -1;
			while((length = fis.read(buf, 0, 1024)) != -1){
				md.update(buf, 0, length);
			}
			fis.close();
			BigInteger bigInt = new BigInteger(1, md.digest());
			return bigInt.toString(16);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 获取文件的MD5
	 * @param filePath	文件路径
	 * @return
	 */
	public static String getMD5CodeByFile(String filePath) {
		return getMD5CodeByFile(new File(filePath));
	}
	
	/**
	 * 获取字符串的MD5
	 * @param str	源字符串
	 * @return
	 */
	public static String getMD5CodeByString(String str) {  
        try {  
            MessageDigest md;  
            md = MessageDigest.getInstance("MD5");  
            byte buf[] = md.digest(str.getBytes());  
            BASE64Encoder encoder = new BASE64Encoder();  
            return encoder.encode(buf);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
            return null;  
        }  
    }

}
