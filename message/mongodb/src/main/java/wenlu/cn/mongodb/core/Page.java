package wenlu.cn.mongodb.core;

import com.mongodb.BasicDBObject;

/**
 * explain	用来存放页码请求信息的参数(考虑到查询条件不统一时无法控制，暂搁置）
 * @author lwl
 * Date 2014年7月10日  Time 下午2:30:04
 */
public class Page {
	
	private final String QueryCacheCollection = "QueryCache";	//查询缓存的表
	
	private MongoBeans mongoBeans;		//一个操作的综合条件类
	
	private int number;			//当前请求的页数
	
	private int rowSize;		//请求的每页显示数
	
	private int oldNumber;		//原来的显示页数
	
	private int oldRowSize;		//原来每页的显示数量
	
	private String lastRowId;	//最后一行的
	
	private BasicDBObject oldOrderKey;	//以前的排序字段以及顺序
	
	private BasicDBObject orderKey;		//现在的排序字段以及顺序
	
	public int getNumber() {
		return number;
	}

	public int getRowSize() {
		return rowSize;
	}

	public int getOldNumber() {
		return oldNumber;
	}

	public int getOldRowSize() {
		return oldRowSize;
	}

	public String getLastRowId() {
		return lastRowId;
	}

	public BasicDBObject getOldOrderKey() {
		return oldOrderKey;
	}

	public BasicDBObject getOrderKey() {
		return orderKey;
	}

	public Page(BasicDBObject obj) {
		if(obj.containsField("page_number")) this.number = obj.getInt("page_number");
		if(obj.containsField("page_rowSize")) this.rowSize = obj.getInt("page_rowSize");
		if(obj.containsField("page_oldNumber")) this.oldNumber = obj.getInt("page_oldNumber");
		if(obj.containsField("page_oldRowSize")) this.oldRowSize = obj.getInt("page_oldRowSize");
		if(obj.containsField("page_lastRowId")) this.lastRowId = obj.getString("page_lastRowId");
		if(obj.containsField("page_oldOrderKey")) this.oldOrderKey = (BasicDBObject) obj.get("page_oldOrderKey");
		if(obj.containsField("page_orderKey")) this.orderKey = (BasicDBObject) obj.get("page_orderKey");
	}
	
	private void saveQuery(MongoBeans mongoBeans){
		MongoManager.getDB().getCollection(QueryCacheCollection)
		.save(new BasicDBObject("_id", mongoBeans.getQueryObj()));
	}
	
	
}
