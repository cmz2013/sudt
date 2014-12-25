package cn.tool.deploy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import cn.tool.config.ToolConfig;
import cn.tool.deploy.scheme.CommandLine;
import cn.tool.deploy.scheme.HostInfo;
import cn.tool.lang.io.MD5Utils;
import cn.tool.shell.SSHShellExcutor;

/**
 * 部署线程
 * 
 * @author chongming
 *
 */
public class DeployThread extends Thread {
	private HostInfo hostInfo; 
	private List<CommandLine> commandList;
	private Log log = null;
	private SSHShellRunner shell = null;
	
	public DeployThread(HostInfo hostInfo, 
			List<CommandLine> commandList, Log log) {
		super();
		this.hostInfo = hostInfo;
		this.commandList = commandList;
		this.log = log;
		this.setName(hostInfo.getIp());
	}

	@Override
	public void run() {
		
		try {
			
			log.info(ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip1") + 
					hostInfo.getIp() + " ... ");
			
			shell = new SSHShellExcutor(hostInfo.getIp(),
					hostInfo.getUserName(), hostInfo.getPassWord());
			shell.connect();
			for (CommandLine command : commandList) {
				try {
					while (DeployManager.isPause()) {
						delay(2000);
					}
					
					delay(command.getLazy());
					if (DeployManager.isStop()) {
						return;
					}
					executeDeploy(shell, command);
				} catch (Exception e) {
					if (command.isRetry()) {
						log.error("[" + command + 
							"] " + ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip2") + 
							"\r\n" + e.getMessage() + 
							"\r\n" + ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip3"));
						shell.close();
						shell.connect();
						executeDeploy(shell, command);
						continue;
					}
					throw e;
				}
			}
			log.info(hostInfo.getIp() + " " + 
					ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip4"));
		} catch (Exception e) {
			log.error(hostInfo.getIp() + " " + 
					ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip5") +
					e.getMessage());
		} finally {
			if (null != shell) {
				shell.close();
			}
			
			while (DeployManager.isPause()) {
				delay(2000);
			}
			
			if (!DeployManager.isStop()) {
				Controller.notify(this);
			}
		}
	}

	private void delay(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void interrupt() {
		try {
			super.interrupt();
		} catch (Exception e) {
			
		} finally {
			if (null != shell) {
				shell.close();
			}
		}
	}
	
	private void executeDeploy (SSHShellRunner shell, 
			CommandLine command) throws Exception {
		String commandLine = command.getCommand().trim();
		try {
			if (commandLine.startsWith("scpPut ")) {
				List<String> cmds = splitCommad(commandLine);
				log.info(ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip6") + " " + 
					cmds.get(1) + "[md5:" +
					MD5Utils.getFileMD5(cmds.get(1)) + 
					"] " + ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip7") + 
					" " + cmds.get(2) + " ... ");
				shell.scpPut(cmds.get(1), cmds.get(2));
				log.info(cmds.get(1) + " " + 
					ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip8"));
				return;
			} else if (commandLine.startsWith("scpGet ")) {
				List<String> cmds = splitCommad(commandLine);
				log.info(ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip9") +
					" " + cmds.get(1) + " " + 
					ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip7") + 
					" " +cmds.get(2) + " ... ");
				shell.scpGet(cmds.get(1), cmds.get(2));
				log.info(cmds.get(1) + 
					"[md5:" + MD5Utils.getFileMD5(getLocalFile(cmds.get(1), cmds.get(2))) + 
					"] " + ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip10"));
				return;
			}
		} catch (Exception e) {
			throw new Exception("[" + commandLine + "] " + 
				ToolConfig.i18.getProperty("soft.deploy.tool.execute.tip11") + 
				e.getMessage());
		}
			
		log.info(shell.execute(commandLine));
	}

	private String getLocalFile(String remoteFile, String localDir) {
		for (int i = remoteFile.length() - 1; i >= 0; i--) {
			if (remoteFile.charAt(i) == '/' || 
				remoteFile.charAt(i) == '\\') {
				remoteFile = remoteFile.substring(i);
				break;
			}
		}
		
		if (localDir.endsWith("/") || localDir.endsWith("\\")) {
			localDir = localDir + remoteFile;
		} else {
			localDir = localDir + File.separator + remoteFile;
		}
		return localDir;
	}

	private List<String> splitCommad(String commandLine) {
		List<String> cmds = new ArrayList<>();
		for (String s : commandLine.split(" ")) {
			if (StringUtils.isNotBlank(s)) {
				cmds.add(s);
			}
		}
		return cmds;
	}

	public Log getLog() {
		return log;
	}
}
