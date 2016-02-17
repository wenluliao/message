package wenlu.cn.mongodb.config;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * explain 读取mongoxml配置。为了能连接多个mongo实例
 * 	暂时废弃，项目先不适用多数据源
 * @author lwl
 * Date 2014年7月18日  Time 下午1:39:58
 */
@SuppressWarnings({"unchecked","rawtypes","static-access"})
@Deprecated
public class XmlConfig {
	
	private static final Logger log = Logger.getLogger(XmlConfig.class);
	
	private static Map<String, MongoClient> mongoClientMap =null;
	
	private static Map<String, String> mongoDefaultDB =null;

	/**
	 * explain 获取指定的多数据源中的数据源 
	 * @param mongoClientName
	 * @return
	 * @author lwl
	 * Date 2014年7月22日  Time 上午8:39:25
	 */
	public static MongoClient getMongoClient(String mongoClientName) {
		if(mongoDefaultDB == null) initConfig(); 
		return mongoClientMap.get(mongoClientName);
	}

	/**
	 * explain 获取指定的多数据源中的数据源 的默认连接数据库
	 * @param defaultDBName
	 * @return
	 * @author lwl
	 * Date 2014年7月22日  Time 上午8:37:03
	 */
	public static String getMongoDefaultDB(String defaultDBName) {
		return mongoDefaultDB.get(defaultDBName);
	}

	/**
	 * explain 初始化多数据源 
	 * 
	 * @author lwl
	 * Date 2014年7月22日  Time 上午8:37:25
	 */
	private static void initConfig(){
		try {
			mongoClientMap = new HashMap<String, MongoClient>();
			mongoDefaultDB = new HashMap<String, String>();
			Document doc = new SAXReader().read(XmlConfig.class.getResourceAsStream("/mongodb.xml"));
			Element rootElement = doc.getRootElement();
			List<Map> mongoClients = (List<Map>) getMap(rootElement);
			log.info("多数据源开始初始化");
			for (int i = 0; i < mongoClients.size(); i++) {
				Map map = mongoClients.get(i);
				List<Map> mogonList = (List<Map>) map.get("mongo");
				Map<String,String> mongoNameMap = mogonList.get(0);
				Map<String,String> defaultDBMap = mogonList.get(1);
				Map<String,String> hostMap = mogonList.get(2);
				Map<String,String> authsMap = mogonList.get(3);
				Map<String,List<Map>> optionsMap = mogonList.get(4);
				log.info("收集数据源-"+mongoNameMap.get("mongoName")+"信息");
				try {
					MongoClient mongoClient ;
					if(!"".equals(authsMap.get("auths")))mongoClient = new MongoClient(getHost(hostMap.get("host")),getAuths(authsMap.get("auths")));
					else mongoClient = new MongoClient(getHost(hostMap.get("host")));
					MongoClientOptions opt = mongoClient.getMongoClientOptions();
					Map<String,String> optMap = new HashMap();
					List<Map> options = optionsMap.get("options");
					for (int j = 0; j < options.size(); j++) {
						if(options.get(j) != null)optMap.putAll(options.get(j));
					}
					opt.builder().connectionsPerHost(new Integer(optMap.get("connectionsPerHost")));
					opt.builder().minConnectionsPerHost(new Integer(optMap.get("minConnectionsPerHost")));
					opt.builder().threadsAllowedToBlockForConnectionMultiplier(new Integer(optMap.get("threadsAllowedToBlockForConnectionMultiplier")));
					opt.builder().maxWaitTime(new Integer(optMap.get("maxWaitTime")));
					opt.builder().maxConnectionIdleTime(new Integer(optMap.get("maxConnectionIdleTime")));
					opt.builder().maxConnectionLifeTime(new Integer(optMap.get("maxConnectionLifeTime")));
					opt.builder().socketTimeout(new Integer(optMap.get("socketTimeout")));
					opt.builder().socketKeepAlive(new Boolean(optMap.get("socketKeepAlive")));
					opt.builder().heartbeatFrequency(new Integer(optMap.get("heartbeatFrequency")));
//					opt.builder().heartbeatConnectRetryFrequency(new Integer(optMap.get("heartbeatConnectRetryFrequency")));
					opt.builder().heartbeatSocketTimeout(new Integer(optMap.get("heartbeatSocketTimeout")));
					opt.builder().heartbeatConnectTimeout(new Integer(optMap.get("heartbeatConnectTimeout")));
//					opt.builder().heartbeatThreadCount(new Integer(optMap.get("heartbeatThreadCount")));
//					opt.builder().acceptableLatencyDifference(new Integer(optMap.get("acceptableLatencyDifference")));
//					opt.builder().requiredReplicaSetName(new String(optMap.get("requiredReplicaSetName")));
					mongoDefaultDB.put(mongoNameMap.get("mongoName"), defaultDBMap.get("defaultDB"));
					mongoClientMap.put(mongoNameMap.get("mongoName"), mongoClient);
				} catch (UnknownHostException e) {
					log.error("多数据源初始化失败，未知的地址",e);
				}
				log.info(mongoNameMap.get("mongoName")+"数据源初始化完成");
			}
			
			log.info("多数据源初始化结束");
			
		} catch (DocumentException e) {
			log.error("多数据源初始化失败",e);
		} catch (Exception e) {
			mongoClientMap = null;
			mongoDefaultDB = null;
		} finally {
		} 
		
	}
	
	private static Object getMap(Element element) {
		if (element.elements().size() > 0) {
			List<Map> maps = new ArrayList<Map>();
			for (Element e : (List<Element>) element.elements()) {
				Map m = new HashMap();
				m.put(e.getName(), getMap(e));
				maps.add(m);
			}
			return maps;
		} else {
			return element.getStringValue();
		}
	}
	
	private static List<ServerAddress> getHost(String host) throws UnknownHostException{
		String[] hosts = host.split(",");
		List<ServerAddress> address = new ArrayList<ServerAddress>();
		for (int i = 0; i < hosts.length; i++) {
			address.add(new ServerAddress(hosts[i]));
		}
		log.info("获取数据集地址完成！");
		return address;
	}
	
	private static List<MongoCredential> getAuths(String auths){
		try {
			List<Map> authInfos = JSONArray.parseArray(auths, Map.class);
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
			log.error("无法读取认证用户，将不使用认证方式登录", e);
			return null;
		}
	}

	
	public static void main(String[] args) {
		System.out.println();
	}
}
