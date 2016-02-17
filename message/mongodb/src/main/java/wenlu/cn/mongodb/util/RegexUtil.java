package wenlu.cn.mongodb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * explain	正则工具
 * @author lwl
 * Date 2014�?�?3�? Time 下午10:12:02
 */
public class RegexUtil {

	/**
	 * explain 正则关键字转�?
	 * @param context	�?��转义的内�?
	 * @return
	 * author lwl
	 * Date 2014�?�?3�? Time 下午10:12:16
	 */
	public static String format(String context){
		Pattern p_char = Pattern.compile("(\\*|\\.|\\?|\\||\\^|\\$|\\+|\\{|\\(|\\[|\\\\)");
		Matcher m_char = p_char.matcher(context);
		while(m_char.find()){  
			String _char = m_char.group();
			context=context.replace(_char,"\\"+_char);
        }
		return context;
	}
}
