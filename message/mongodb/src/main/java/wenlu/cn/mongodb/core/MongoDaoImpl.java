package wenlu.cn.mongodb.core;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * explain mongo简单操作的封装
 * @author lwl
 * Date 2014年7月15日  Time 下午9:04:11
 */
public class MongoDaoImpl   {
	
	
	/**
	 * explain mongo的新增数据(在默认的数据库中操作)
	 * @param beans	
	 * @param collection 集合名称
	 * @author lwl
	 * Date 2014年7月15日  Time 下午9:06:22
	 */
	public void insert(MongoBeans beans,String collection){
		MongoManager.getDB().getCollection(collection).save(beans.getDataObj());
	}
	
	/**
	 * explain mongo的新增数据
	 * @param dbName 数据库名称
	 * @param beans
	 * @param collection 集合名称
	 * @author lwl
	 * Date 2014年7月15日  Time 下午9:12:08
	 */
	public void insert(String dbName ,MongoBeans beans,String collection){
		MongoManager.getDB(dbName).getCollection(collection).save(beans.getDataObj());
	}
	
	/**
	 * explain mongo的更新数据(在默认的数据库中操作)
	 * @param beans
	 * @param collection 集合名称
	 * @author lwl
	 * Date 2014年7月15日  Time 下午9:10:47
	 */
	public void update(MongoBeans beans,String collection){
		MongoManager.getDB().getCollection(collection).update(beans.getQueryObj(), beans.getDataObj(), false, true);
	}
	
	/**
	 * explain mongo的更新数据
	 * @param dbName 数据库名
	 * @param beans
	 * @param collection 集合名称
	 * @author lwl
	 * Date 2014年7月15日  Time 下午9:21:46
	 */
	public void update(String dbName ,MongoBeans beans,String collection){
		MongoManager.getDB(dbName).getCollection(collection).update(beans.getQueryObj(), beans.getDataObj(), false, true);
	}
	
	/**
	 * explain 查询一条数据(在默认的数据库中操作)
	 * @param beans
	 * @param collection
	 * @return
	 * @author lwl
	 * Date 2014年7月15日  Time 下午9:16:05
	 */
	public DBObject searchOne(MongoBeans beans,String collection){
		DBObject findOne = MongoManager.getDB().getCollection(collection).findOne(beans.getQueryObj(),beans.getFiledObj());
		return findOne;
	}
	
	/**
	 * explain 查询一条数据(在默认的数据库中操作)
	 * @param dbName 数据库名
	 * @param beans
	 * @param collection 集合名称
	 * @return
	 * @author lwl
	 * Date 2014年7月15日  Time 下午9:21:07
	 */
	public DBObject searchOne(String dbName,MongoBeans beans,String collection){
		return MongoManager.getDB(dbName).getCollection(collection).findOne(beans.getQueryObj(),beans.getFiledObj());
	}
	
	/**
	 * explain 删除数据 (在默认的数据库中操作)
	 * @param beans
	 * @param collection
	 * @author lwl
	 * Date 2014年7月21日  Time 下午4:21:07
	 */
	public void remove(MongoBeans beans,String collection){
		MongoManager.getDB().getCollection(collection).remove(beans.getQueryObj());
	}
	
	/**
	 * explain 删除数据 
	 * @param dbName	数据库名称
	 * @param beans		
	 * @param collection	表名称
	 * @author lwl
	 * Date 2014年7月21日  Time 下午4:21:52
	 */
	public void remove(String dbName,MongoBeans beans,String collection){
		MongoManager.getDB(dbName).getCollection(collection).remove(beans.getQueryObj());
	}
	
	/**
	 * explain 查询多条数据，放在游标中(在默认的数据库中操作)
	 * @param beans
	 * @param collection
	 * @return
	 * @author lwl
	 * Date 2014年7月16日  Time 上午9:04:50
	 */
	public DBCursor searchList(MongoBeans beans,String collection){
		if(beans.getPageObj() != null)
			return MongoManager.getDB().getCollection(collection).
				find(beans.getQueryObj(),beans.getFiledObj()).sort(beans.getOrderObj()).
				limit(beans.getPageObj().getLimit()).skip(beans.getPageObj().getSkip());
		else
			return MongoManager.getDB().getCollection(collection).
					find(beans.getQueryObj(),beans.getFiledObj()).sort(beans.getOrderObj());
	}
	
	
	/**
	 * explain 查询多条数据，放在游标中
	 * @param dbName 数据库名
	 * @param beans
	 * @param collection 集合名称
	 * @return
	 * @author lwl
	 * Date 2014年7月16日  Time 上午9:06:52
	 */
	public DBCursor searchList(String dbName,MongoBeans beans,String collection){
		
		if(beans.getPageObj() != null)
			return MongoManager.getDB(dbName).getCollection(collection).
					find(beans.getQueryObj(),beans.getFiledObj()).sort(beans.getOrderObj()).
					limit(beans.getPageObj().getLimit()).skip(beans.getPageObj().getSkip());
		else
			return MongoManager.getDB(dbName).getCollection(collection).
					find(beans.getQueryObj(),beans.getFiledObj()).sort(beans.getOrderObj());
		
	}

	/**
	 * explain  查询总数据量
	 * @param beans
	 * @param collection
	 * @return
	 * @author lwl
	 * Date 2014年7月16日  Time 下午2:45:59
	 */
	public Integer getCount(MongoBeans beans,String collection){
		return MongoManager.getDB().getCollection(collection).find(beans.getQueryObj()).count();
	}
	
	
	public static void main(String[] args) {
		try {
			DB db2 = MongoManager.getDB();
			System.out.println(JSONArray.toJSONString(db2.getMongo().getDatabaseNames()));
			System.out.println(db2.getName());
			System.out.println(JSONArray.toJSONString(db2.getCollectionNames()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
