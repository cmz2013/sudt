package cn.tool.deploy.scheme;

import java.io.Serializable;

/**
 * 命令行
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class CommandLine implements Serializable {
	/**
	 * 命令表达式
	 */
	private String command;
	/**
	 * 当command执行失败时，重新连接并下发该命令
	 */
	private boolean retry = false;
	
	/**
	 * 延迟执行命令， 单位：毫秒
	 */
	private int lazy = 0;

	public CommandLine(String command, boolean retry, int lazy) {
		super();
		this.command = command;
		this.retry = retry;
		this.lazy = lazy;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	public int getLazy() {
		return lazy;
	}

	public void setLazy(int lazy) {
		this.lazy = lazy;
	}

	@Override
	public String toString() {
		return command + "\n" + retry + "\n" + lazy;
	}
	
}
