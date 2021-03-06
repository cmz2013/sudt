package cn.sudt.deploy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.sudt.config.ToolConfig;
/**
 * 并发任务控制器
 * 
 * @author chongming
 *
 */
public class ConcurrentController {
	
	/**
	 * LinkedList实现了Queue接口，使用offer()
	 * 来加入元素，使用poll()来获取并移出元素。
	 */
	private LinkedList<DeployThread> pool = new LinkedList<DeployThread>();
	/**
	 * 部署过程结束
	 */
	private volatile boolean end = true;
	
	/**
	 * 正在执行部署的线程
	 */
	private List<DeployThread> currentTask = new ArrayList<DeployThread>();
	
	private DeployManager deployManager = null;
	
	public ConcurrentController(DeployManager deployManager) {
		super();
		this.deployManager = deployManager;
	}

	/**
	 * 并发部署，阻塞过程
	 * 
	 */
	public void execute() throws Exception {
		
		end = false;
		
		while (pool.size() > 0 && 
				currentTask.size() < ToolConfig.deploy.getConcurrent()) {
			DeployThread thread = pool.poll();
			thread.start();
			currentTask.add(thread);
		}
		deployManager.setCurrentTaskInfo();
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
	public void interrupt () {
		for (DeployThread thread : currentTask) {
			thread.interrupt();
		}
	}

	/**
	 * 部署线程调用该方法通知Controller，hostIp部署过程已结束
	 * 
	 * @param hostIp
	 */
	public synchronized void notify(DeployThread thread) {
		currentTask.remove(thread);
		if (!deployManager.isInterrupted() && pool.size() > 0) {
			thread = pool.poll();
			currentTask.add(thread);
			thread.start();
		}
		
		deployManager.setCurrentTaskInfo();
		
		/*
		 * 所有并发任务已完成
		 */
		if (0 == currentTask.size()) {
			end = true;
		}
	}

	public LinkedList<DeployThread> getPool() {
		return pool;
	}

	public synchronized List<DeployThread> getCurrentTask() {
		return new ArrayList<DeployThread>(currentTask);
	}

	public boolean isEnd() {
		return end;
	}
	
}
