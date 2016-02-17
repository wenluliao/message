package wenlu.cn.mongodb.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * explain 适用于web接口返回内容
 * @author lwl
 * Date 2014�?�?5�? Time 下午3:07:28
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ResultMap extends HashMap {

	private static final long serialVersionUID = 1L;
	
	/**
	 * explain 适合无返回内容输�?
	 * @param successFlag	执行是否成功
	 * @param message	返回信息
	 * @author lwl
	 * Date 2014�?�?5�? Time 下午3:04:55
	 */
	public ResultMap(Boolean successFlag,String message){
		this.put("success", successFlag);	
		this.put("msg", message);
	}

	/**
	 * explain 适合无分页内容输�?
	 * @param successFlag	执行是否成功
	 * @param message	返回信息
	 * @param data		返回内容
	 * @author lwl
	 * Date 2014�?�?5�? Time 下午3:05:41
	 */
	public ResultMap(Boolean successFlag,String message,Object data){
		this.put("success", successFlag);
		this.put("msg", message);
		this.put("data", data);
	}
	
	/**
	 * explain 
	 * @param successFlag	执行是否成功
	 * @param message	返回信息
	 * @param data		
	 * @author zhangbo
	 */
	public ResultMap(Boolean successFlag,String message,Object dataOne,Object dataTwo){
		this.put("success", successFlag);
		this.put("msg", message);
		this.put("data", dataOne);
		this.put("typeArray", dataTwo);
	}
	
	public  ResultMap(String total,List<Map> list){
		this.put("total", total);
		this.put("rows", list);
	}
	
	/**
	 * explain 适合分页查询输出
	 * @param successFlag	执行是否成功
	 * @param message	返回信息
	 * @param data	返回内容
	 * @param total	总数
	 * @author lwl
	 * Date 2014�?�?5�? Time 下午3:05:59
	 */
	public ResultMap(Boolean successFlag,String message,Object data,Integer total){
		this.put("success", successFlag);
		this.put("msg", message);
		this.put("data", data);
		this.put("total", total);
	}
	
}
