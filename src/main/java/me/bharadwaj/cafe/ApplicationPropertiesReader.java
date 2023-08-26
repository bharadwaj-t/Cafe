package me.bharadwaj.cafe;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// source: https://baeldung.com/java-accessing-maven-properties
public class ApplicationPropertiesReader {
	private static final String PROPERTY_FILE_NAME = "application.properties";
	private static final Properties PROPERTIES = new Properties();
	private static final InputStream inputStream = ApplicationPropertiesReader.class.getClassLoader()
			.getResourceAsStream(PROPERTY_FILE_NAME);

	public static String getProperty(String propertyName) {
		try {
			PROPERTIES.load(inputStream);
			return PROPERTIES.getProperty(propertyName);
		} catch (IOException e) {
			System.out.println("Couldn't fetch properties file to load: " + e);
		}
		
		return "";
	}
}
