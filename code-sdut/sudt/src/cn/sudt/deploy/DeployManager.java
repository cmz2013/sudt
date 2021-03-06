package cn.sudt.deploy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.sudt.config.ToolConfig;
import cn.sudt.deploy.scheme.DeployScheme;
import cn.sudt.deploy.scheme.HostInfo;
import cn.sudt.lang.io.Log4jConfig;
import cn.sudt.lang.io.MD5Utils;
import cn.sudt.ui.tab.DeployTabbed;

/**
 * 部署管理器(部署过程为阻塞过程，加入到线程中)
 * 
 * @author chongming
 *
 */
public class DeployManager extends Thread {
	/**
	 * 通过共享变量控制线程并发
	 * 
	 * 暂停部署过程
	 */
	private volatile boolean pause = false;
	/**
	 * 中断部署过程
	 */
	private volatile boolean interrupted = false;
	
	private ConcurrentController concurrentController = null;
	private DeployTabbed deployTab = null;
	private Log4jConfig logConfig = new Log4jConfig();
	private MD5Utils md5Utils = new MD5Utils();
	
	public DeployManager(DeployTabbed deployTab) {
		super();
		this.deployTab = deployTab;
		this.concurrentController = new ConcurrentController(this);
	}
	
	@Override
	public void run() {
		executeDeploy();
	}
	
	/**
	 * 创建部署线程，执行部署
	 */
	private void executeDeploy() {
		
		try {
			DeployScheme deployScheme = getDeployScheme();
			String configPath = ToolConfig.USER_DIR + "/cfg/log4j.properties";
			List<String> logName = new ArrayList<String>();
			
			for (int i = 0; i < deployScheme.getHostList().size(); i++) {
				HostInfo hostInfo = deployScheme.getHostList().get(i);
				logName.add(hostInfo.getIp() + "_" + hostInfo.getPort() + ".log");
			}
			
			logConfig.initLogConfig(configPath, true, 
					ToolConfig.LOG_DIR, logName.toArray(new String[0]));
			
			for (int i = 0; i < deployScheme.getHostList().size(); i++) {
				HostInfo hostInfo = deployScheme.getHostList().get(i);
				Log log = LogFactory.getLog("logFile" + i);
				DeployThread deployThread = new DeployThread(this,
						hostInfo, deployScheme.getCommandList(), log);
				concurrentController.getPool().offer(deployThread);
			}
			
			/**
			 * 部署线程并发执行
			 */
			concurrentController.execute();
			
			if (interrupted) {
				JOptionPane.showMessageDialog(null, 
						ToolConfig.i18.getProperty("sudt.deploy.interrupt"), 
						ToolConfig.i18.getProperty("sudt.title.tip"), 
						JOptionPane.OK_OPTION);
			} else {
				JOptionPane.showMessageDialog(null, 
						ToolConfig.i18.getProperty("sudt.deploy.end"), 
						ToolConfig.i18.getProperty("sudt.title.tip"), 
						JOptionPane.OK_OPTION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			deployTab.setBtnStatus(true);
		}
	}
	
	private DeployScheme getDeployScheme() {
		DeployScheme deployScheme = new DeployScheme();
		deployScheme.setHostList(deployTab.getHostTablePanel().getHosts());
		deployScheme.setCommandList(deployTab.getCmdTablePanel().getCommands());
		return deployScheme;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
		if (pause) {
			deployTab.getBottomPanel().setBusyLabelText(
				ToolConfig.i18.getProperty("sudt.deploy.pause"));
		} else {
			setCurrentTaskInfo();
		}
	}
	
	/**
	 * 窗口提示正在部署的ip:port
	 * 
	 */
	public void setCurrentTaskInfo() {
		String text = "";
		for (Thread thread : concurrentController.getCurrentTask()) {
			text += thread.getName() + "/";
		}
		
		if (text.length() > 1) {
			text = text.substring(0, text.length() - 1);
		}
		
		deployTab.getBottomPanel().setBusyLabelText(text);
	}

	public boolean isPause() {
		return pause;
	}

	public boolean isInterrupted() {
		return interrupted;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	public DeployTabbed getDeployTab() {
		return deployTab;
	}

	public ConcurrentController getConcurrentController() {
		return concurrentController;
	}

	public MD5Utils getMd5Utils() {
		return md5Utils;
	}
	
}
