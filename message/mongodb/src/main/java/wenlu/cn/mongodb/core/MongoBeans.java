package wenlu.cn.mongodb.core;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import org.bson.types.ObjectId;



import wenlu.cn.mongodb.util.RegexUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * explain	专门用来处理更新对象的Bean
 * @author lwl
 * Date 2014年7月8日  Time 上午11:32:25
 */
public class MongoBeans {
	
	/**
	 * 查询条件对象
	 */
	private BasicDBObject queryObj;
	
	/**
	 * 更新对象
	 */
	private BasicDBObject dataObj;
	
	/**
	 * 排序对象
	 */
	private BasicDBObject orderObj;
	
	/**
	 * 字段过滤对象
	 */
	private BasicDBObject filedObj;


	/**
	 *	分页对象 
	 */
	private MongoPage pageObj;
	
	
	/**
	 * explain 获取你所需要的排序对象 
	 * @return
	 * @author lwl
	 * Date 2014年7月16日  Time 下午2:41:43
	 */
	public BasicDBObject getOrderObj() {
		return orderObj;
	}
	
	/**
	 * explain 获取你所需要的分页对象 
	 * @return
	 * @author lwl
	 * Date 2014年7月16日  Time 下午2:48:47
	 */
	public MongoPage getPageObj() {
		return pageObj;
	}


	/**
	 * explain 获取你所需要的查询对象
	 * @return
	 * @author lwl
	 * Date 2014年7月8日  Time 上午11:32:00
	 */
	public BasicDBObject getQueryObj() {
		return queryObj;
	}


	/**
	 * explain 获取你所需要更新的对象
	 * @return
	 * @author lwl
	 * Date 2014年7月8日  Time 上午11:32:58
	 */
	public BasicDBObject getDataObj() {
		return dataObj;
	}

	/**
	 * explain 获取你查询所需要的过滤字段对象
	 * @return 
	 * @author wenluliao
	 * <br/>Date 2014-11-7  Time 下午3:47:07
	 */
	public BasicDBObject getFiledObj() {
		return filedObj;
	}

	/**
	 * explain 处理数据 将查询对象和更新对象分离。并处理模糊查询数据  进行智能分页查询，识别数据索引以及排序	(未完成方法，暂时废弃)
	 * @param map	参数要求 （查询条件search_开头，需要模糊查询的字段 用 likecolumn 指定，逗号隔开。排序字段order_开头，分页信息page_开头）
	 * @author lwl
	 * Date 2014年7月8日  Time 上午11:29:25
	 */
	@Deprecated
	public MongoBeans(Map<String,Object> map){
		this.disposeData(new BasicDBObject(map));
	}
	
	/**
	 * explain 根据类型处理数据 
	 * @param operateType
	 * @param map
	 * @author lwl
	 * Date 2014年7月16日  Time 下午4:58:29
	 */
	public MongoBeans(OperateType operateType,Map<String,?> map){
		//jdk 1.7以上支持
		switch (operateType.getOperateType()) {
		case "insert":
			this.disposeDataInsert(new BasicDBObject(map));
			break;
		case "update":
			this.disposeDataUpdate(new BasicDBObject(map));
			break;
		case "remove":
			
			break;
		case "search":
			this.disposeDataSearch(new BasicDBObject(map));
			break;
		default:
			break;
		} 
	}
	
	/**
	 * explain 处理数据，解析查询，模糊查询，更新对象
	 * @param map
	 * @author lwl
	 * Date 2014年7月9日  Time 下午4:39:47
	 */
	public MongoBeans(BasicDBObject map){
		this.disposeData(map);
	}
	
	/**
	 * explain 根据类型处理数据
	 * @param operateType
	 * @param map
	 * @author lwl
	 * Date 2014年7月16日  Time 下午4:58:03
	 */
	public MongoBeans(OperateType operateType,BasicDBObject map){
		//jre 1.7以上支持
		switch (operateType.getOperateType()) {
		case "insert":
			this.disposeDataInsert(map);
			break;
		case "update":
			this.disposeDataUpdate(map);
			break;
		case "remove":
			this.disposeDataSearch(map);
			break;
		case "search":
			this.disposeDataSearch(map);
			break;
		default:
			break;
		} 
//		String type = operateType.getOperateType();
//		if("insert".equals(type)){
//			this.disposeDataInsert(map);
//		}else if("update".equals(type)){
//			this.disposeDataUpdate(map);
//		}else if("remove".equals(type)){
//			this.disposeDataSearch(map);
//		}else{
//			this.disposeDataSearch(map);
//		}
		
	}
	
	
	/**
	 * explain 处理数据 将查询对象和更新对象分离。并处理模糊查询数据  进行智能分页查询，识别数据索引以及排序	(未完成方法，暂时废弃)
	 * @param map	参数要求 （查询条件search_开头，需要模糊查询的字段 用 likecolumn 指定，逗号隔开。排序字段order_开头，分页信息page_开头）
	 * @return
	 * @author lwl
	 * Date 2014年7月8日  Time 上午11:34:44
	 */
	@Deprecated
	private void disposeData(BasicDBObject map){
		String[] columns = null;	//准备获取所有模糊查询的字段
		BasicDBObject setObj = (BasicDBObject) dataObj.get("$set");	//构建字段独立更新结构
		
		//处理模糊查询
		if(map.containsField("likecolumn") && map.get("likecolumn") != null && !"".equals(map.get("likecolumn").toString())){
			columns = map.get("likecolumn").toString().split(",");
		}else map.remove("likecolumn");
		
		
		//处理查询条件和更新数据
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while(it.hasNext()){
		    Entry<String, Object>  entry=it.next();
		    String key=entry.getKey();
		    Object value=entry.getValue();
		    if(key.startsWith("search_")){
				queryObj.put(key.replaceFirst("search_", ""), value);	//将所有以search_开头的key都作为查询条件
			}else if(key.startsWith("order_")){
				orderObj.put(key.replaceFirst("order_", ""), value);	//将所有以order_开头的key都作为排序条件
			}
		    
		    else{
				setObj.put(key, value);		//剩下的都作为更新条件
			}
		}
		
		//处理查询中的模糊查询条件
		if(columns != null){
			for (int i = 0; i < columns.length; i++) {
				if(this.queryObj.containsField(columns[i])){
					this.queryObj.put(columns[i], new BasicDBObject("$regex", "^"+this.queryObj.get(columns[i])+".*"));
				}
			}
		}
	}
	
	/**
	 * explain 处理查询数据
	 * @param map
	 * @author lwl
	 * Date 2014年7月15日  Time 上午11:11:42
	 */
	private void disposeDataSearch(BasicDBObject map){
		String[] orColumns = null;  //准备需要并集查询的字段
		String[] columns = null;	//准备获取所有模糊查询的字段
		this.queryObj = new BasicDBObject();
		this.filedObj = new BasicDBObject();
		
		//处理模糊查询
		if(map.containsField("likecolumn") && map.get("likecolumn") != null && !"".equals(map.get("likecolumn").toString())){
			columns = map.get("likecolumn").toString().split(",");
		}
		map.remove("likecolumn");
		
		//处理并集查询
		if(map.containsField("orcolumn") && map.get("orcolumn") != null && !"".equals(map.get("orcolumn").toString())){
			orColumns = map.get("orcolumn").toString().split(",");
		}
		map.remove("orcolumn");
		
		//获得分页对象
		if(map.containsField("page") && map.containsField("rows")){
			if(map.get("page") instanceof String)this.pageObj = new MongoPage(Integer.parseInt(map.get("page").toString()),Integer.parseInt(map.get("rows").toString()));
			else this.pageObj = new MongoPage((Integer)map.get("page"),(Integer)map.get("rows"));
			map.remove("page");
			map.remove("rows");
		}
		
		//处理排序数据
		if(map.containsField("sort")){
			orderObj = new BasicDBObject();		//初始化排序存储对象
			String valuestr = map.getString("sort");	//获取排序字段
			String[] values = valuestr.split(",");
			Arrays.stream(values).forEach((value) -> {
				String order = value.substring(0,1);	//获取排序方式
				String filed = value.substring(1, value.length());//获取需要排序的列名
				orderObj.put(filed, "+".equals(order)?1:-1);//开头为+的是顺序，-的是倒序。约定规则
			});
	    	map.remove("sort");
		}else orderObj = new BasicDBObject();
		
		//处理需要显示的字段
		if(map.containsField("filtration")){
			String[] filed = map.getString("filtration").split(",");
			for (int i = 0; i < filed.length; i++) {
				this.filedObj.append(filed[i], 1);
			}
			map.remove("filtration");
		}else if(map.containsField("nofiltration")){
			String[] filed = map.getString("nofiltration").split(",");
			for (int i = 0; i < filed.length; i++) {
				this.filedObj.append(filed[i], 0);
			}
			map.remove("nofiltration");
		}
		
		//单独处理_id
		if(map.containsField("_id")){
	    	if(map.get("_id") instanceof String) this.queryObj.put("_id", new ObjectId(map.getString("_id")));
	    	map.remove("_id");
	    }
		
		
		//处理查询条件
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while(it.hasNext()){
		    Entry<String, Object>  entry=it.next();
		    if(entry.getValue() != null && !"".equals(entry.getValue()))
		    	this.queryObj.put(entry.getKey(), entry.getValue());		//剩下的都作为更新条件
		}
		
		//处理查询中的模糊查询条件
		if(columns != null){
			for (int i = 0; i < columns.length; i++) {
				if(this.queryObj.containsField(columns[i])){
					this.queryObj.put(columns[i], new BasicDBObject("$regex", "^"+RegexUtil.format(this.queryObj.get(columns[i]).toString())+".*"));
				}
			}
		}
		
		//处理并集查询条件
		if(orColumns != null){
			DBObject orObj = new BasicDBObject("$or",new BasicDBList());
			BasicDBList orList = (BasicDBList)orObj.get("$or");
			for (int i = 0; i < orColumns.length; i++) {
				if(this.queryObj.containsField(orColumns[i])){
					orList.add(new BasicDBObject(orColumns[i],this.queryObj.get(orColumns[i])));
				}
				this.queryObj.remove(orColumns[i]);
			}
			this.queryObj.putAll(orObj);
		}
	}
	
	/**
	 * explain 处理新增数据 
	 * @param map
	 * @author lwl
	 * Date 2014年7月15日  Time 下午10:02:36
	 */
	private void disposeDataInsert(BasicDBObject map){
		this.dataObj = map;
	}
	
	/**
	 * explain 处理更新数据 
	 * @param map
	 * @author lwl
	 * Date 2014年7月15日  Time 下午9:54:04
	 */
	private void disposeDataUpdate(BasicDBObject map){
		this.dataObj = new BasicDBObject("$set",new BasicDBObject());
		this.queryObj = new BasicDBObject();
		BasicDBObject setObj = (BasicDBObject) dataObj.get("$set");	//构建字段独立更新结构
		
		this.queryObj.put("_id", new ObjectId(map.getString("_id")));
		map.removeField("_id");
		
		//处理查询条件
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while(it.hasNext()){
		    Entry<String, Object>  entry=it.next();
		    if(entry.getKey().indexOf("$") == -1)	//判断是否有特殊操作符字段
		    	setObj.put(entry.getKey(), entry.getValue());		//剩下的都作为更新条件
		    else
		    	this.dataObj.append(entry.getKey(), entry.getValue());
		}
		
	}
	
	public static void main(String[] args) {
		BasicDBObject obj = new BasicDBObject().append("a", 1).append("b", 2).append("c", 3);
		obj.append("d", new BasicDBObject("dd", "ddd"));
		System.out.println(obj.get("d.dd"));
	}
}
