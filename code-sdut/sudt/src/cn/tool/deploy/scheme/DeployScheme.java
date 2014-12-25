package cn.tool.deploy.scheme;

import java.io.Serializable;
import java.util.List;

/**
 * 部署方案
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class DeployScheme implements Serializable {
	/**
	 * 主机列表
	 */
	private List<HostInfo> hostList;
	/**
	 * 命令清单，顺序执行
	 */
	private List<CommandLine> commandList;

	public List<HostInfo> getHostList() {
		return hostList;
	}

	public void setHostList(List<HostInfo> hostList) {
		this.hostList = hostList;
	}

	public List<CommandLine> getCommandList() {
		return commandList;
	}

	public void setCommandList(List<CommandLine> commandList) {
		this.commandList = commandList;
	}
	
}
