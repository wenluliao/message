package wenlu.cn.mongodb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * explain	æ­£åˆ™å·¥å…·
 * @author lwl
 * Date 2014å¹?æœ?3æ—? Time ä¸‹åˆ10:12:02
 */
public class RegexUtil {

	/**
	 * explain æ­£åˆ™å…³é”®å­—è½¬ä¹?
	 * @param context	éœ?¦è½¬ä¹‰çš„å†…å®?
	 * @return
	 * author lwl
	 * Date 2014å¹?æœ?3æ—? Time ä¸‹åˆ10:12:16
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
