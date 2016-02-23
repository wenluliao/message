package wenlu.cn.beans.base;

public class UserLogin {
	private Long userId;
	private String loginName;
	private String password;
	private String hex;
	private int lastLoginType;
	private String loginMsgIp;
	private int loginMsgPort;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHex() {
		return hex;
	}
	public void setHex(String hex) {
		this.hex = hex;
	}
	public int getLastLoginType() {
		return lastLoginType;
	}
	public void setLastLoginType(int lastLoginType) {
		this.lastLoginType = lastLoginType;
	}
	public String getLoginMsgIp() {
		return loginMsgIp;
	}
	public void setLoginMsgIp(String loginMsgIp) {
		this.loginMsgIp = loginMsgIp;
	}
	public int getLoginMsgPort() {
		return loginMsgPort;
	}
	public void setLoginMsgPort(int loginMsgPort) {
		this.loginMsgPort = loginMsgPort;
	}
	
}
