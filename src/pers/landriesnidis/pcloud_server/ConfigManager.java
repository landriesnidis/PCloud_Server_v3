package pers.landriesnidis.pcloud_server;

import java.io.IOException;

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
    	PropertiesHelper helper = new PropertiesHelper("configuration.properties");
		try {
			helper.readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FILE_SAVE_PATH = helper.getValue("FILE_SAVE_PATH");
		FILE_TEMP_PATH = helper.getValue("FILE_TEMP_PATH");
		helper = null;
    }
}
