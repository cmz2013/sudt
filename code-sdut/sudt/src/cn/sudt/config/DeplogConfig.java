package cn.sudt.config;
/**
 * 软件部署过程相关配置
 * 
 * @author chongming
 *
 */
public class DeplogConfig {
	/**
	 * 部署线程并发数
	 */
	private int concurrent = 2;
	/**
	 * 命令最大延迟执行时间(秒)
	 */
	private int maxDelay = 60;
	/**
	 * 失败重试
	 */
	private boolean retry = false;
	/**
	 * SSH客户端编码
	 */
	private String sshEncode;
	/**
	 * 执行命令超时(毫秒)
	 */
	private int timeOut = 60000;
	
	public int getConcurrent() {
		return concurrent;
	}
	
	public int getMaxDelay() {
		return maxDelay;
	}
	
	public void setMaxDelay(int maxDelay) {
		this.maxDelay = maxDelay;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	public void setConcurrent(int concurrent) {
		this.concurrent = concurrent;
	}

	public String getSshEncode() {
		return sshEncode;
	}

	public void setSshEncode(String sshEncode) {
		this.sshEncode = sshEncode;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut * 60000;
	}

}
