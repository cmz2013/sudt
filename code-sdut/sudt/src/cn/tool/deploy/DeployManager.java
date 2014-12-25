package cn.tool.deploy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.tool.config.ToolConfig;
import cn.tool.deploy.scheme.DeployScheme;
import cn.tool.deploy.scheme.HostInfo;
import cn.tool.lang.io.Log4jConfig;
import cn.tool.ui.resour.SystemConst;
import cn.tool.ui.tab.DeployTabbed;

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
	private volatile static boolean pause = false;
	/**
	 * 终止部署过程
	 */
	private volatile static boolean stop = true;
	
	private static DeployTabbed deployTab = null;

	@Override
	public void run() {
		executeDeploy();
	}
	
	/**
	 * 创建部署线程，执行部署
	 */
	private void executeDeploy() {
		DeployScheme deployScheme = getDeployScheme();
		String configPath = ToolConfig.USER_DIR + "/cfg/log4j.properties";
		List<String> logName = new ArrayList<String>();
		
		for (int i = 0; i < deployScheme.getHostList().size(); i++) {
			HostInfo hostInfo = deployScheme.getHostList().get(i);
			logName.add("deploy_" + hostInfo.getIp() + ".log");
		}
		
		Log4jConfig.initLogConfig(configPath, true, 
				ToolConfig.LOG_DIR, logName.toArray(new String[0]));
		
		try {
			Controller.getPool().clear();
			for (int i = 0; i < deployScheme.getHostList().size(); i++) {
				HostInfo hostInfo = deployScheme.getHostList().get(i);
				Log log = LogFactory.getLog("logFile" + i);
				DeployThread deployThread = new DeployThread(hostInfo,
						deployScheme.getCommandList(), log);
				Controller.getPool().offer(deployThread);
			}
			
			/**
			 * 部署线程并发执行
			 */
			Controller.concurrent();
			
			if (stop) {
				deployTab.getBottomPanel().setBusyLabelText(ToolConfig.i18.getProperty(
						"soft.deploy.tool.deploy.interrupt"));
				deployTab.getCmdTablePanel().getCmdExeBtn().setEnabled(true);
			} else {
				deployTab.getBottomPanel().setBusyLabelText(ToolConfig.i18.getProperty(
						"soft.deploy.tool.deploy.end"));
			}
			deployTab.setBtnStatus(true);
			Thread.sleep(SystemConst.MES_VALID_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private DeployScheme getDeployScheme() {
		DeployScheme deployScheme = new DeployScheme();
		deployScheme.setHostList(deployTab.getHostTablePanel().getHosts());
		deployScheme.setCommandList(deployTab.getCmdTablePanel().getCommands());
		return deployScheme;
	}

	public static void setPause(boolean pause) {
		DeployManager.pause = pause;
		if (pause) {
			deployTab.getBottomPanel().setBusyLabelText(
				ToolConfig.i18.getProperty("soft.deploy.tool.deploy.pause"));
		} else {
			Controller.setBottomPanelText();
		}
	}

	public static boolean isPause() {
		return pause;
	}

	public static boolean isStop() {
		return stop;
	}

	public static void setStop(boolean stop) {
		DeployManager.stop = stop;
	}

	public static DeployTabbed getDeployTab() {
		return deployTab;
	}

	public static void setDeployTab(DeployTabbed deployTab) {
		DeployManager.deployTab = deployTab;
	}
	
}
