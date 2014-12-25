package cn.tool.lang.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/*
 *  对于properties文件，
 *  如果key不存在Properties的方法getProperties(String key)返回null
 *  如果key存在但value空，则getProperties返回""
 */
public class PropertyUtils {
	/**
	 * 获取处理properties配置文件Properties类的实例
	 * @param filePath properties配置文件路径
	 * @return Properties类的实例
	 * @throws IOException
	 */
	public static Properties getProperties(String filePath) throws IOException {
		InputStream in = null;
		  try {
			  Properties prop = new Properties();
			   in = new FileInputStream(filePath);
			   prop.load(in);
			   return prop;
		} catch (IOException e) {
			throw e;
		} finally {
			/**
			    * 流使用结束后，关闭流并且释放相关资源调用close()。为了程序安全，
			    * 也为了提高计算机的资源利用率，流使用结束后，程序应及时关闭它
			    */
			   if (in != null) in.close();
		}
	}
	
}
