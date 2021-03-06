package cn.sudt.deploy.scheme;

import java.io.Serializable;

/**
 * 主机信息
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class HostInfo implements Serializable {
	/**
	 * 主机IP
	 */
	private String ip;
	/**
	 * 端口
	 */
	private int port = 22;
	/**
	 * 主机用户名
	 */
	private String userName;
	/**
	 * 用户密码
	 */
	private String passWord;
	
	public HostInfo(String ip, String userName, String passWord) {
		super();
		this.ip = ip;
		this.userName = userName;
		this.passWord = passWord;
	}
	
	public HostInfo(String ip, int port, String userName, String passWord) {
		super();
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.passWord = passWord;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassWord() {
		return passWord;
	}
	
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	@Override
	public String toString() {
		return ip + "\r\n" + port + "\r\n" + 
				userName + "\r\n" + passWord;
	}
}
