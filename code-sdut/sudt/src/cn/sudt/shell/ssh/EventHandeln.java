package cn.sudt.shell.ssh;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.lang.StringUtils;

import cn.sudt.config.ToolConfig;
import cn.sudt.lang.io.CharsetDetector;


/**
 * 事件解析
 * 
 * @author chongming
 */
public class EventHandeln {
	/**
	 * 请求输入参数
	 */
	public static final String[] PROMPT_ARG = { ":", "?" };
	/**
	 * 准备执行命令
	 */
	public static final String[] PROMPT_CMD = { "$", "#", ">", "%" };

	/**
	 * 执行命令返回的信息
	 */
	private ByteArrayOutputStream stream = new ByteArrayOutputStream();
	
	/**
	 * 根据执行命令返回的信息判断特定事件：请求用户输入
	 * 
	 * @return
	 */
	public boolean isRequestInput(String res) {
		if (StringUtils.isNotBlank(res)) {
			for (String flag : PROMPT_ARG) {
				boolean cmdFlag = res.endsWith(flag);
				if (cmdFlag) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 执行命令返回的结果
	 * 
	 * @return 注意：返回结果为乱码时，将无法识别命令执行完毕或需要输入参数
	 */
	public String getResult() {
		try {
			if (StringUtils.isBlank(ToolConfig.deploy.getSshEncode())) {
				return new CharsetDetector().detect(
						stream.toByteArray()).trim();
			} else {
				return new String(stream.toByteArray(),
					ToolConfig.deploy.getSshEncode()).trim();
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 接收执行命令时返回的字节
	 * 
	 * @param b
	 */
	public synchronized void receiveData(byte b) {
		stream.write(b);
	}

	public void clear() {
		stream.reset();
	}

	/**
	 * 根据执行命令返回的信息判断特定事件：命令执行完毕
	 * 
	 * @return
	 */
	public boolean isFinish(String res) {
		if (StringUtils.isNotBlank(res)) {
			for (String flag : PROMPT_CMD) {
				boolean cmdFlag = res.endsWith(flag);
				if (cmdFlag) {
					return true;
				}
			}
		}
		return false;
	}
}
