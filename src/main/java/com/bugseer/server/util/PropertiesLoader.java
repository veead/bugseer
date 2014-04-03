package com.bugseer.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author vee on Aug 30, 2013 at 3:55:45 PM
 *
 */
public class PropertiesLoader {

	private static Logger log = LogManager.getLogger(PropertiesLoader.class);
	private static Properties props;

	public static Properties getProperties() {
		if (props == null)
			props = loadProperties();
		return props;
	}

	private static Properties loadProperties() {
		Properties props = new Properties();
		InputStream inputStream = PropertiesLoader.class.getClassLoader()
				.getResourceAsStream("app.properties");
		try {
			props.load(inputStream);
		} catch (IOException e) {
			log.error("File not Found " + e.getMessage());
		}
		return props;
	}
}
