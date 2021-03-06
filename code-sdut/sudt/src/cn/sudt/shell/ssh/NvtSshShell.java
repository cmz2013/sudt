package cn.sudt.shell.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import cn.sudt.config.ToolConfig;


import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 虚拟SSH shell
 * 
 * @author chongming
 */
public class NvtSshShell implements Runnable {
	
	private volatile boolean isSessionDown = false;
	private volatile EventHandeln dispatcher = new EventHandeln();

	/**
	 * shell返回的信息
	 */
	private InputStream in;
	/**
	 * 向shell发送数据
	 */
	private OutputStream out;

	private Thread thread;

	private Connection conn = null;

	private Session session = null;

	public NvtSshShell(ShellCertificate cert)
			throws Exception {
		thread = new Thread(this);

		conn = new Connection(cert.getIp(), cert.getPort());
		conn.connect();
		if (!conn.authenticateWithPassword(cert.getUserName(), cert.getPsw()))
			throw new IOException("Authentication failed.");

		session = conn.openSession();
		session.requestPTY("dumb", 99999, 0, 0, 0, null);
		session.startShell();

		in = new StreamGobbler(session.getStdout());
		out = session.getStdin();
		
		thread.start();
		delay(200);
	}
	
	public boolean isSessionDown() {
		return isSessionDown;
	}

	/**
	 * 向shell发送数据
	 * @param data
	 * @throws IOException
	 */
	private void sendData(byte[] data) throws IOException {
		out.write(data, 0, data.length);
		out.flush();
	}

	public void run() {
		int b;
		try {
			while ((b = in.read()) != -1) {
				dispatcher.receiveData((byte) b);
			}
			isSessionDown = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String execute(String cmd) throws Exception {
		if (cmd == null || cmd.trim().equals("")) {
			throw new Exception("Command cannot be empty");
		}

		if (isSessionDown()) {
			close();
			throw new Exception("Session is down");
		}
		
		dispatcher.clear();
		sendData((cmd + "\n").getBytes());
		
		int timeOut = ToolConfig.deploy.getTimeOut() / 500;
		
		while (true) {
			timeOut--;
			delay(500);
			String res = dispatcher.getResult();
			if (dispatcher.isRequestInput(res)) {
				String arg = JOptionPane.showInputDialog(null,
						dispatcher.getResult(), null);
				
				sendData((arg + "\n").getBytes());
				timeOut = ToolConfig.deploy.getTimeOut() / 500;
			} else if (dispatcher.isFinish(res)) {
				return res;
			} else if (timeOut < 0) {
				throw new Exception("[" + cmd + "]"
					+ " time out, return: \n" + res);
			}
		}
	}

	private void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	public Connection getConnection() {
		return conn;
	}

	/**
	 * 关闭shell连接
	 */
	public void close() {
		isSessionDown = true;
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			if (session != null) {
				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
