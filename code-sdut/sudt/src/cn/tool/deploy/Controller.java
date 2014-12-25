package cn.tool.deploy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.tool.config.ToolConfig;
/**
 * 并发控制
 * 
 * @author chongming
 *
 */
public class Controller {
	
	/**
	 * LinkedList实现了Queue接口，使用offer()
	 * 来加入元素，使用poll()来获取并移出元素。
	 */
	private static LinkedList<DeployThread> pool = new LinkedList<DeployThread>();
	/**
	 * 部署过程结束
	 */
	private static volatile boolean end = true;
	
	/**
	 * 正在部署的线程
	 */
	private static List<DeployThread> deployThread = new ArrayList<DeployThread>();

	/**
	 * 部署线程并发执行
	 * 
	 */
	public static void concurrent() throws Exception {
		
		end = false;
		
		while (pool.size() > 0 && 
				deployThread.size() < ToolConfig.deploy.getConcurrent()) {
			DeployThread thread = pool.poll();
			thread.start();
			deployThread.add(thread);
		}
		setBottomPanelText();
		/**
		 * 等待部署完成
		 */
		while (!end) {
			Thread.sleep(2000);
		}
	}
	
	/**
	 * 中断部署过程
	 */
	public static void interrupt () {
		
		for (DeployThread thread : deployThread) {
			thread.interrupt();
		}
		
		end = true;
		deployThread.clear();
		pool.clear();
	}

	/**
	 * 部署线程调用该方法通知Controller，hostIp部署过程已结束
	 * 
	 * @param hostIp
	 */
	public static synchronized void notify(DeployThread thread) {
		deployThread.remove(thread);
		if (pool.size() > 0 && !end) {
			thread = pool.poll();
			thread.start();
			deployThread.add(thread);
		}
		setBottomPanelText();
	}

	/**
	 * 窗口提示正在部署的IP
	 * 
	 */
	public static void setBottomPanelText() {
		String text = "";
		for (Thread thread : deployThread) {
			text += thread.getName() + "/";
		}
		
		if (text.length() > 1) {
			text = text.substring(0, text.length() - 1);
			DeployManager.getDeployTab().getBottomPanel(
					).setBusyLabelText(text);
		} else {
			end = true;
		}
	}
	
	public static LinkedList<DeployThread> getPool() {
		return pool;
	}
}
