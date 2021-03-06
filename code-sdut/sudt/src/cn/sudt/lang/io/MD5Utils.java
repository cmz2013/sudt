package cn.sudt.lang.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
/**
 * MD5工具类
 * 
 * @author chongming
 *
 */
public class MD5Utils {
	
	public String getStrMD5(String str) throws Exception {
		if (null == str) return null;
		String res = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', 
				'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		byte tmp[] = md.digest(); 
		char rec[] = new char[16 * 2];
		int k = 0; 
		for (int i = 0; i < 16; i++) {
			byte byte0 = tmp[i];
			rec[k++] = hexDigits[byte0 >>> 4 & 0xf];
			rec[k++] = hexDigits[byte0 & 0xf];
		}
		res = new String(rec); 

		return res;
	}
	
	public String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	
	public String getRARMD5(String rarPath) throws IOException {
		FileInputStream in = new FileInputStream(rarPath); 
		int l = in.available(); 
		byte[] b = new byte[l]; 
		in.read(b); 
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} 
		digest.update(b); 
		byte[] result = digest.digest(); 
		BigInteger bigInt = new BigInteger(1, result);
		return bigInt.toString(16);
	}
	
	public String getFileMD5(String fileName) {
		return getFileMD5(new File(fileName));
	}
	

	public Map<String, String> getDirMD5(
			File file, boolean listChild) {
		
		if (!file.isDirectory()) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String md5;
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory() && listChild) {
				map.putAll(getDirMD5(f, listChild));
			} else {
				md5 = getFileMD5(f);
				if (md5 != null) {
					map.put(f.getPath(), md5);
				}
			}
		}
		return map;
	}
}