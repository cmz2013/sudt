package cn.tool.shell;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import cn.tool.shell.ssh.NvtSshShell;
import cn.tool.shell.ssh.ShellCertificate;

/**
 * 远程调用shell命令或可执行程序
 * 
 * @author chongming
 */
public class SSHShellExcutor {
	
	private NvtSshShell nvt;
	private ShellCertificate cert;
	
	public SSHShellExcutor(String ip, String userName, String psw) {
		cert = new ShellCertificate();
		cert.setIp(ip);
		cert.setPort(22);
		cert.setUserName(userName);
		cert.setPsw(psw);
	}

	public SSHShellExcutor(String ip, int port, String userName, String psw) {
		cert = new ShellCertificate();
		cert.setIp(ip);
		cert.setPort(port);
		cert.setUserName(userName);
		cert.setPsw(psw);
	}
	
	/**
	 * 从其他网络计算机中拿去文件
	 * 
	 * 该方法执行前须调用connect打开连接，
	 * 执行结束后，须要调用close关闭连接
	 * 
	 * @param remoteFile
	 * @param localDir
	 * @throws IOException
	 */
	public void scpGet(String remoteFile, 
			String localDir) throws Exception { 
		
		Connection conn = nvt.getConnection();
		SCPClient client = new SCPClient(conn); 
		client.get(remoteFile, localDir); 
	}
	
	/**
	 * 将文件拷贝到其他计算机上
	 * 
	 * 该方法执行前须调用connect打开连接，
	 * 执行结束后，须要调用close关闭连接
	 * 
	 * @param localFile
	 * @param remoteDir
	 * @throws IOException
	 */
	public void scpPut(String localFile, 
			String remoteDir) throws Exception { 
		
		Connection conn = nvt.getConnection();
		SCPClient client = new SCPClient(conn);
		client.put(localFile, remoteDir);
	}
	
	/**
	 * 连接shell
	 * @throws Exception
	 */
	public void connect() throws Exception {
		nvt = new NvtSshShell(cert);
	}
	
	/**
	 * 该方法执行前须调用connect打开连接，
	 * 执行结束后，须要调用close关闭连接
	 * @param cmd
	 * @return
	 * @throws Exception 
	 */
	public String execute(String cmd) throws Exception {
		return nvt.execute(cmd);
	}
	
	/**
	 * 释放shell资源
	 */
	public void close() {
		if (nvt != null) {
			nvt.close();
		}
	}
	
}
