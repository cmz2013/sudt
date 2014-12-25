package cn.tool.shell.ssh;

/**
 * 封装远程连接认证信息
 * 
 * @author chongming
 */
public class ShellCertificate {
	private String ip;

	private int port;

	private String userName;

	private String psw;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
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
}
