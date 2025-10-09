package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
	
	private static Properties properties;
	private static final String CONFIG_FILE="src/main/resources/config.properties";
	
	static {
		properties = new Properties();
		
		try {
			FileInputStream input = new FileInputStream(CONFIG_FILE);
			properties.load(input);
			input.close();
		} catch(Exception e) {
			throw new RuntimeException("Failed to Read Config File");
		}
	}
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

}
