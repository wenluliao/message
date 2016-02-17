package wenlu.cn.scoket.mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class MongoConfig {
	private MongoConfig() {
	}

	private static final Logger log = Logger.getLogger(MongoConfig.class);
	private static final String fileName = "/mongo.properties";
	private static final Properties properties = new Properties();;
	static {
		try {
			InputStream inputStream = MongoConfig.class.getResourceAsStream(fileName);
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
