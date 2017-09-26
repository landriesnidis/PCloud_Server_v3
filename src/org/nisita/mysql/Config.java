package org.nisita.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	private Config() {}
	
	private static Properties p;
	static {
		p = new Properties();
		InputStream is = Config.class.getResourceAsStream("connection.properties");
		try {
			p.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getDriver() {
		return p.getProperty("driver");
	}
	public static String getUser() {
		return p.getProperty("user");
	}
	public static String getPassword() {
		return p.getProperty("password");
	}
	public static String getUrl() {
		return p.getProperty("url");
	}
}
