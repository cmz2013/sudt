package cn.sudt.lang.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import cn.sudt.config.ToolConfig;

/**
 * 设置log4j配置文件
 * 
 * @author chongming
 *
 */
public class Log4jConfig {
	/**
	 * @param configPath：properties配置文件路径
	 * @param isAppendLogger: true，表示以logFile0为模板，
			  追加logFilei配置(i=1, ..., logName.length-1)
	 * @param logDir：日志目录
	 * @param logName[i]: 日志文件名,对应于logNamei
	 */
	public void initLogConfig (
			String configPath, boolean isAppendLogger,
			String logDir, String... logName) {
		
		Properties logPro = new Properties();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(configPath));
			System.setProperty("logDir", logDir);
			String encode = StringUtils.isBlank(ToolConfig.deploy.getSshEncode(
					)) ? "UTF-8" : ToolConfig.deploy.getSshEncode(); 
			System.setProperty("encode", encode);
			
			for(int i = 0; i < logName.length; i++) {
				System.setProperty("logName" + i, logName[i]);
			}
			
			if (isAppendLogger) {
				List<String> list = new ArrayList<String>();
				String line = reader.readLine();
				while (null != line) {
					if (!"".equals(line.trim()) && !line.startsWith("#")) {
						String[] keyValue = line.split("=");
						String key = keyValue[0].trim();
						String value = keyValue[1].trim();
						logPro.put(key, value);
						if ("log4j.logger.logFile0".equals(key)
								|| "log4j.appender.logFile0".equals(key)
								|| key.startsWith("log4j.appender.logFile0.")) {
							list.add(key);
							list.add(value);
						}
					}
					line = reader.readLine();
				}
				
				for(int i = 1; i < logName.length; i++) {
					for (int j = 0; j < list.size(); j++) {
						String key = list.get(j).replace("logFile0", "logFile" + i);
						String value = list.get(++j).replace("logFile0", 
								"logFile" + i).replace("logName0", "logName" + i);
						logPro.put(key, value);
					}
				}
			} else {
				logPro.load(reader);
			}
			
			PropertyConfigurator.configure(logPro);
		} catch (Exception e) {
			System.err.println("log4j.properties load error! " + e.getMessage());
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}
	}
	
	/**
	 * @param configPath：properties配置文件路径
	 * @param logDir：日志目录
	 */
	public void initLogConfig (String configPath, String logDir) {
		System.setProperty("logDir", logDir);
		InputStream inStream = null;
		try {
			inStream = new FileInputStream(configPath);
			Properties logPro = new Properties();
			logPro.load(inStream);
			PropertyConfigurator.configure(logPro);
		} catch (Exception e) {
			System.err.println("log4j.properties load error! " + e.getMessage());
		} finally {
			try {
				if (null != inStream) {
					inStream.close();
				}
			} catch (IOException e) {

			}
		}
	}
}
