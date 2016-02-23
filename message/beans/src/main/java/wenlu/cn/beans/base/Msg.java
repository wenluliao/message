package wenlu.cn.beans.base;

import org.bson.types.ObjectId;

import com.alibaba.fastjson.JSON;

public class Msg {
	private String msgId;
	private long userId;
	private String content;
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
