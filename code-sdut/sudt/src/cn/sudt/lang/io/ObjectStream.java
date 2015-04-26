package cn.sudt.lang.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 对象流工具类
 * 
 * @author CHONGMING
 *
 */

/*
 * 序列化是一种用来处理对象流的机制，
 * 是为了解决在对象流进行读写操作时所引起的问题
 * 
 * 所谓对象流也就是将对象的内容进行流化，可以对流化后的对象
 * 进行读写操作，也可将流化后的对象传输于网络之间
 * 
 * 序列化的实现：将需要被序列化的类实现Serializable接口，该接口
 * 没有需要实现的方法，implements Serializable只是为了标注该对象
 * 是可序列化的。
 * 
 * 对象流读写操作：
 * 使用一个输出流(如：FileOutputStream)来构造一个ObjectOutputStream(对象流)对象，
 * 接着，使用ObjectOutputStream对象的writeObject(Object obj)方法就可以将obj写出
 * (即保存其状态)，要恢复的话，用输入流
 */
public class ObjectStream {
	
	/**
	 * 将objects写入文件
	 * @param file
	 * @param objects
	 * @throws Exception
	 */
	public void writeObject(File file, 
			Object ...objects) throws Exception {
		
		if (null != objects && objects.length > 0) {
			FileOutputStream fos = null;
			ObjectOutputStream oos = null;
			
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			
			for (Object obj : objects) {
				if (obj instanceof Serializable) {
					oos.writeObject(obj);
				} else {
					if (null != oos) {
						oos.flush();
						oos.close();
					}
					
					if (null != fos) {
						fos.close();
					}
					
					file.deleteOnExit();
					throw new Exception("Object must implement Serializable");
				}
			}
			
			/*
			 * 用于ObjectInputStream.readObject判断输入流结束
			 */
			oos.writeObject(null);
			
			if (null != oos) {
				oos.flush();
				oos.close();
			}
			
			if (null != fos) {
				fos.close();
			}
		}
	}
	
	/**
	 * 从文件读取对象
	 * 
	 * @param clazz
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	public List<?> readObject(
			Class<?> clazz, File file) throws Exception {
		
		List<Object> list = new ArrayList<Object>();
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			
			Object obj = ois.readObject();
			while (null != obj) {
				list.add(obj);
				obj = ois.readObject();
			}
		} catch(Exception e) {
			throw e;
		} finally {
			if (null != ois) {
				ois.close();
			}
			
			if (null != fis) {
				fis.close();
			}
		}
		return list;
	}
	
	
	/**
	 * 从流读取对象
	 * 
	 * @param clazz
	 * @param inputStream
	 * @return
	 * @throws Exception 
	 */
	public List<?> readObject(Class<?> clazz, 
			InputStream inputStream) throws Exception {
		
		List<Object> list = new ArrayList<Object>();
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(inputStream);
			
			Object obj = ois.readObject();
			while (null != obj) {
				list.add(obj);
				obj = ois.readObject();
			}
		} catch(Exception e) {
			throw e;
		} finally {
			if (null != ois) {
				ois.close();
			}
		}
		return list;
	}
}
