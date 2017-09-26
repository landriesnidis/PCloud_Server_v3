package pers.landriesnidis.pcloud_server.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * properties配置文件读取助手
 * @author landriesnidis
 * 修改时间：2017-5-29 11:01:00
 */
public class PropertiesHelper {
	
	private HashMap<String, String> hashMap = new HashMap<String, String>();
	private String fileName;
	
	public PropertiesHelper() {}
	
	public PropertiesHelper(String fileName) {
		this.fileName = fileName;
	}
	
	public PropertiesHelper(InputStream in) {
		try {
			readFile(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过键获取值
	 * @param key
	 * @return 记录值
	 */
	public String getValue(String key){
		if(!hashMap.containsKey(key))
			return null;
		return hashMap.get(key);
	}
	
	/**
	 * 新增或修改记录
	 * @param key
	 * @param value
	 */
	public void setValue(String key,String value){
		hashMap.put(key, value);
	}
	
	/**
	 * 清空操作
	 */
	public void clear(){
		hashMap.clear();
		fileName = null;
	}
	
	/**
	 * 判断默认文件是否存在
	 * @return
	 */
	public boolean exist(){
		return new File(fileName).exists();
	}
	
	/**
	 * 判断指定文件是否存在
	 * @param fileName	文件路径
	 * @return
	 */
	public boolean exist(String fileName){
		return new File(fileName).exists();
	}
	
	/**
	 * 读取默认配置文件
	 * @throws IOException
	 */
	public void readFile() throws IOException{
		if(fileName==null){
			throw new FileNotFoundException("Unspecified file name.");
		}
		readFile(fileName);
	}
	
	/**
	 * 读取指定配置文件
	 * 默认配置文件会被修改为新指定的配置文件
	 * @param fileName 文件名(包含文件格式后缀)
	 * @throws IOException 指定文件不存在/被占用
	 */
	public void readFile(String fileName) throws IOException {
		this.fileName = fileName;
		hashMap.clear();
		InputStream in = new BufferedInputStream(new FileInputStream(fileName));
		readFile(in);
	}
	
	private void readFile(InputStream in) throws IOException {
		Properties prop = new Properties();
		prop.load(in); //加载属性列表
		//遍历配置文件将数据转存至HashMap中
		Iterator<String> it = prop.stringPropertyNames().iterator();
		while (it.hasNext()) {
			String key = it.next();
			hashMap.put(key, prop.getProperty(key));
		}
		in.close();
	}
	
	/**
	 * 保存修改
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void save() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		if(fileName==null){
			throw new FileNotFoundException("Unspecified file name.");
		}
		FileOutputStream fos = new FileOutputStream(fileName);
		//遍历HashMap
		Iterator<Entry<String, String>> iter = hashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			prop.setProperty(entry.getKey(), entry.getValue());
		}
		//写入文件
		prop.store(fos, null);
		fos.close();
	}
	
	/**
	 * 将文件另存为指定文件名
	 * @param newFileName
	 * @throws IOException
	 */
	public void saveAs(String newFileName) throws IOException{
		Properties prop = new Properties();
		FileOutputStream fos = new FileOutputStream(newFileName,true);
		//遍历HashMap
		Iterator<Entry<String, String>> iter = hashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			prop.setProperty(entry.getKey(), entry.getValue());
		}
		//写入文件
		prop.store(fos, null);
		fos.close();
	}
	
	public HashMap<String, String> getHashMap() {
		return hashMap;
	}
}

