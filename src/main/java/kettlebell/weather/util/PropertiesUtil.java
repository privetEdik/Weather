package kettlebell.weather.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	private static final Properties PROPERTIES;

	static {
		PROPERTIES = new Properties();
		try {
			File file = new File(PropertiesUtil.class.getClassLoader().getResource("properties").getFile());
			FileReader fileReader = new FileReader(file);
			PROPERTIES.load(fileReader);
		} catch (Exception e) {
			throw new RuntimeException("invalid load properties");
		}
	}

	public static String getProperty(String key) {
		return PROPERTIES.getProperty(key);
	}
}
