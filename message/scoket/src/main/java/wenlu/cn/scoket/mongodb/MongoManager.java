package wenlu.cn.scoket.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

/**
 * explain mongo实例
 * @author lwl
 * Date 2014年7月9日  Time 上午11:24:14
 */
@SuppressWarnings({"static-access","rawtypes"})
public class MongoManager {
	private static final Logger log = Logger.getLogger(MongoManager.class);
	private static MongoClient mongoClient =null;
	private MongoManager() {
	}

	/**
	 * explain 获取一个指定数据库的连接对象
	 * @param dbName
	 * @return
	 * @author lwl
	 * Date 2014年7月9日  Time 上午11:24:35
	 */
	public static MongoDatabase getDB(String dbName) {
		if (mongoClient == null) {
			init();
		}
		return mongoClient.getDatabase(dbName);
	}
	
	/**
	 * explain 初始化数据连接，检验是否认真
	 * @return
	 * @author lwl
	 * Date 2014年7月15日  Time 下午5:06:58
	 */
	public static MongoDatabase getDB() {
		if (mongoClient == null) {
			init();
		}
		return mongoClient.getDatabase(MongoConfig.get("defaultDB"));
	}
	
	/**
	 * explain 获取mongo实例端
	 * @return
	 * @author lwl
	 * Date 2014年7月9日  Time 上午11:25:01
	 */
	public static MongoClient getMongoClient(){
		if(mongoClient ==null){
			init();
		}
		return mongoClient;
	}
	
	

	/**
	 * explain mongo实例初始化 
	 * 
	 * @author lwl
	 * Date 2014年7月9日  Time 上午11:25:21
	 */
	private static void init() {
		String host = MongoConfig.get("host");	//访问地址

		try {
			List<MongoCredential> auths = getAuths();
			if(auths != null)mongoClient=new MongoClient(getHost(host),getAuths());		//设置需要访问的数据库实例以及各个数据库的认证用户
			else mongoClient=new MongoClient(getHost(host));
			MongoClientOptions opt = mongoClient.getMongoClientOptions();
			opt.builder().connectionsPerHost(new Integer(MongoConfig.get("connectionsPerHost")));
			opt.builder().minConnectionsPerHost(new Integer(MongoConfig.get("minConnectionsPerHost")));
			opt.builder().threadsAllowedToBlockForConnectionMultiplier(new Integer(MongoConfig.get("threadsAllowedToBlockForConnectionMultiplier")));
			opt.builder().maxWaitTime(new Integer(MongoConfig.get("maxWaitTime")));
			opt.builder().maxConnectionIdleTime(new Integer(MongoConfig.get("maxConnectionIdleTime")));
			opt.builder().maxConnectionLifeTime(new Integer(MongoConfig.get("maxConnectionLifeTime")));
			opt.builder().socketTimeout(new Integer(MongoConfig.get("socketTimeout")));
			opt.builder().socketKeepAlive(new Boolean(MongoConfig.get("socketKeepAlive")));
			opt.builder().heartbeatFrequency(new Integer(MongoConfig.get("heartbeatFrequency")));
			opt.builder().heartbeatSocketTimeout(new Integer(MongoConfig.get("heartbeatSocketTimeout")));
			opt.builder().heartbeatConnectTimeout(new Integer(MongoConfig.get("heartbeatConnectTimeout")));
			opt.builder().requiredReplicaSetName(new String(MongoConfig.get("requiredReplicaSetName")));
			log.info("mongo 初始化完成！");
		} catch (UnknownHostException e) {
			log.error("mongo 初始化失败！\n未知异常地址\t：\t\t"+host,e);
		} catch (MongoException e) {
			log.error("mongo 初始化失败！\nmongo实例异常！",e);
		}
	}
	

	/**
	 * explain 注销实例
	 * 
	 * @author lwl
	 * Date 2014年7月9日  Time 上午11:31:09
	 */
	public static void close() {
		if (mongoClient != null) {
			mongoClient.close();
			mongoClient = null;
		}
	}
	
	/**
	 * explain 获取地址，支持单机，集群
	 * @param host
	 * @return
	 * @throws UnknownHostException
	 * @author lwl
	 * Date 2014年7月9日  Time 上午11:31:30
	 */
	private static List<ServerAddress> getHost(String host) throws UnknownHostException{
		String[] hosts = host.split(",");
		List<ServerAddress> address = new ArrayList<ServerAddress>();
		for (int i = 0; i < hosts.length; i++) {
			address.add(new ServerAddress(hosts[i]));
		}
		log.info("获取数据集地址完成！");
		return address;
	}
	
	/**
	 * explain 获取各个数据库的认证用户 
	 * @return
	 * @author lwl
	 * Date 2014年7月15日  Time 下午7:51:54
	 */
	private static List<MongoCredential> getAuths(){
		try {
			List<Map> authInfos = JSONArray.parseArray(new String(MongoConfig.get("auths")), Map.class);
			List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();  
			for (int i = 0; i < authInfos.size(); i++) {
				credentialsList.add(MongoCredential.createMongoCRCredential(
						authInfos.get(i).get("username").toString(),
						authInfos.get(i).get("dbname").toString(),
						authInfos.get(i).get("password").toString().toCharArray()));
			}
			log.info("读取认证用户完成！");
			return credentialsList;
		} catch (Exception e) {
			log.error("无法读取认证用户，将不使用认证方式登录");
			return null;
		}
	}
}
