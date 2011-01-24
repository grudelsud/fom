package fom.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyHandler {
	
	private static PropertyHandler instance;
	private Properties properties;
	
	private PropertyHandler(){
		FileInputStream fos = null;
		try {
			fos = new FileInputStream("data/properties.properties");
			properties = new Properties();
			properties.load(fos);
		} catch (FileNotFoundException e) {
			System.err.println("There was a problem loading the properties file, make sure it is in data/properties.properties");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("There was a problem loading the properties file, make sure it is in data/properties.properties");
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static PropertyHandler getInstance(){
		if(instance == null){
			instance = new PropertyHandler();
		}
		return instance;
	}
	
	private Properties getProperties(){
		return properties;
	}
	
	public static String getStringProperty(String propertyName){
		return PropertyHandler.getInstance().getProperties().getProperty(propertyName);
	}
	
	public static int getIntProperty(String propertyName){
		return Integer.parseInt(PropertyHandler.getInstance().getProperties().getProperty(propertyName));
	}
}
