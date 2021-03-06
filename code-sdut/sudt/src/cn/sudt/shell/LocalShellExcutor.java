package cn.sudt.shell;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.sudt.lang.io.CharsetDetector;

/**
 * 通过Runtime接口调用本地shell命令或可执行程序
 * 
 * 1. 	在window下可以直接执行一个.exe文件，如执行我在F盘下的tomcat安装文件，将命令写为：
 * 		String cmd = "F:\\apache-tomcat-6.0.20.exe";
 * 2. 	打开一个word文档。如果系统已经安装了office应用程序，就可以通过调用word的可执行程序来打开一个word文档：
 *  	String cmd = "D:\\Program Files\\Microsoft Office\\OFFICE11\\WINWORD.EXE F:\\test.doc";
 *  	当然这样写有点麻烦，我们想打开一个word文档时只要双击就可以了，用不着去找WINWORD.EXE。
 *  	要是打开每一种格式的文件都得去找它的可执行程序，那可累死了，我们可以通过下面的代码，打开任意一个已知格式的文件
 *  	（只要安装的打开这种文件格式的软件），相当于用鼠标双击一个文件的图标：
 *  	 String cmd = "cmd.exe /c start F:\\test.doc";
 *  3.	 用C写了一个可执行文件，将编译出的可执行文件"fork_wait"放在 linux 下，
 *  	String cmd = "./fork_wait";
 */
public class LocalShellExcutor {
	
	private CharsetDetector detector = new CharsetDetector();

	/**
	 * 执行shell命令
	 * 
	 * @param cmd
	 * @return
	 * @throws Exception 
	 */
	public String[] executeReturnArray(String cmd) throws Exception {
		String str = executeReturnString(cmd);
		if (null != str) {
			return str.split(System.getProperty("line.separator"));
		}
		return null;
	}

	/**
	 * 执行shell命令
	 * 
	 * @param cmd
	 * @return String
	 * @throws Exception 
	 */
	public String executeReturnString(String cmd) throws Exception {
		String result = null;
		Process process = null;
		BufferedInputStream reader = null;
		InputStream ios = null;
		try {
			process = getProcess(cmd);
			ios = process.getInputStream();
			process.getErrorStream().close();
			process.getOutputStream().close();
			
			reader = new BufferedInputStream(ios);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buf = new byte[256];
			int len = -1;
			while ((len = reader.read(buf)) != -1) {
				stream.write(buf, 0, len);
			}
			result = detector.detect(stream.toByteArray()).trim();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
				
				if (null != ios) {
					ios.close();
				}
				
				if (process != null) {
					process.destroy();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 执行shell命令
	 * 
	 * @param cmd
	 * @return int 0：表示执行成功，其他为失败
	 * @throws Exception 
	 */
	public int execute(String cmd) throws Exception {
		int exitValue = -1;
		Process process = null;
		try {
			process = getProcess(cmd);
			/* 
			 * 平台的InputStream和OutputStream的size是有限的，
			 * 如果不及时处理掉就会把程序block住
			 */
			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
			
			// 等待执行完成并退出
			exitValue = process.waitFor();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (process != null) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return exitValue;
	}

	/**
	 * 构造子进程
	 * 
	 * @param cmd  调用的命令
	 * @throws IOException
	 * @return Process
	 */
	private Process getProcess(String cmd) throws IOException {
		// window
		if (System.getProperty("os.name").toLowerCase().indexOf("window") != -1) {
			return Runtime.getRuntime().exec("cmd /c " + cmd);
		} 
		
		// linux
		if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) {
			return Runtime.getRuntime().exec(
					new String[] { "/bin/sh", "-c", cmd });
		}
		
		return null;
	}
	
	/**
	 * 返回Linux服务器启动时间
	 * @return
	 * @throws Exception 
	 */
	public String getLinuxBootTime() throws Exception {
		String cmd = "date -d \"$(awk -F. '{print $1}' /proc/uptime) second ago\" +\"%Y/%m/%d,%H:%M:%S\"";
		String result = executeReturnString(cmd);
		return result;
	}
	
	/**
	 * 返回Linux服务器正常运行时间
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String getLinuxUptime() throws Exception {
		String cmd = "uptime";
		String result = executeReturnString(cmd);
		result = result.substring(result.indexOf("up") + 3, result.indexOf("user"));
		result = result.substring(0, result.lastIndexOf(","));
		return result;
	}
}
