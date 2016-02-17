package wenlu.cn.mongodb.core;


public class MongoPage {
	
	private int skip;
	
	private int limit;
	
	public int getSkip() {
		return skip;
	}

	public int getLimit() {
		return limit;
	}

	/**
	 * explain 处理简单分页参数（页数，行数） 
	 * @param page
	 * @param pagesize
	 * @author lwl
	 * Date 2014年7月9日  Time 下午5:09:53
	 */
	public MongoPage(int number,int rowsize){
		this.skip = number*rowsize - rowsize;
		this.limit = rowsize;
	}
	
	/**
	 * explain 处理高性能分页参数	(未完成方法，暂时废弃)
	 * @param oldNumber		旧页数
	 * @param oldRowSize	旧行数
	 * @param lastRowId		旧的最后一条记录的主键id
	 * @param number		新页数
	 * @param rowSize		新页的行数
	 * @author lwl
	 * Date 2014年7月10日  Time 上午8:36:37
	 */
	@Deprecated
	public MongoPage(int oldNumber,int oldRowSize,String lastRowId , 
			int number,int rowSize){
		int temSkip = oldNumber*oldRowSize-1;
		if(oldNumber==number){
			this.skip = temSkip-oldRowSize;
			this.limit = rowSize;
		}
		
	}
	
}
