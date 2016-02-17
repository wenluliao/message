package wenlu.cn.mongodb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class Config {
	private Config() {
	}

	private static final Logger log = Logger.getLogger(Config.class);
	private static final String fileName = "/mongo.properties";
	private static final Properties properties = new Properties();;
	static {
		try {
			InputStream inputStream = Config.class.getResourceAsStream(fileName);
			properties.load(inputStream);
		} catch (IOException e) {
			log.error("加载配置文件失败！", e);
		} catch (Throwable e) {
			log.error("加载配置文件失败！", e);
		}
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}
}
