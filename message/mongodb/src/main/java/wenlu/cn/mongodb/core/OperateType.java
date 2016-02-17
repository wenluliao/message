package wenlu.cn.mongodb.core;

/**
 * explain mongo操作类型
 * @author lwl 
 * Date 2014年7月15日 Time 上午10:38:06
 */
public enum OperateType {

	insert("insert"), update("update"), search("search"), remove("remove");

	private String operateType;

	public String getOperateType() {
		return operateType;
	}

	private OperateType(String operateType) {
		this.operateType = operateType;
	}

}
