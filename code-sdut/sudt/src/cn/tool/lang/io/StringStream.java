package cn.tool.lang.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 字符串流操作
 * 
 * @author chongming
 *
 */
public class StringStream {

	/**
	 * 将字符串写入文件
	 * 
	 * @param str
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static void writeString(String str, 
			File file) throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(
				new FileOutputStream(file), "UTF-8");
		osw.write(str);
		osw.flush();
		osw.close();
	}

}
