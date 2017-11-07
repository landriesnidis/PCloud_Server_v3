package pers.landriesnidis.pcloud_server;

import java.io.File;

import pers.landriesnidis.pcloud_server.utils.PropertiesHelper;

public class ConfigManager {

    private static final ConfigManager manager = new ConfigManager();  
    //静态工厂方法   
    public static ConfigManager getManager() {  
        return manager;  
    }
    
    public final String FILE_SAVE_PATH;		//文件保存路径
	public final String FILE_TEMP_PATH;		//临时文件路径
    
    private ConfigManager() {
    	PropertiesHelper helper = new PropertiesHelper(ConfigManager.class.getResourceAsStream("configuration.properties"));
		FILE_SAVE_PATH = helper.getValue("FILE_SAVE_PATH");
		FILE_TEMP_PATH = helper.getValue("FILE_TEMP_PATH");
		
		File file;
		file = new File(FILE_SAVE_PATH);
		if(!file.exists())file.mkdirs();
		file = new File(FILE_TEMP_PATH);
		if(!file.exists())file.mkdirs();
    }
}
