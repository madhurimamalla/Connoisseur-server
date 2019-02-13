package com.github.madhurimamalla.connoisseur.server.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfigMain {

	private InputStream inputStream;
	private Properties prop;

	public ReadConfigMain() {
	}

	public Properties getPropValues(String fileName) {

		try {
			prop = new Properties();
			inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + fileName + "' not found!");
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

}
