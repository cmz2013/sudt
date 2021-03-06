package cn.sudt.lang.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密和解密工具类
 * 
 * 文件流采用UTF-8编码
 * 
 * @author chongming
 * 
 */
public class CryptoUtils {
	/*
	 * AES算法基于排列和置换运算。排列是对数据重新进行安排，
	 * 置换是将一个数据单元替换为另一个。
	 */
	public static final String ALG_STRING = "AES";
	public static final String ALG_STRING_CIPHER = "AES/CBC/PKCS5Padding";

	// 指定一个初始化向量
	private final byte[] initvector = { (byte) 0xcb, (byte) 0x53,
			(byte) 0x03, (byte) 0x0f, (byte) 0xe0, (byte) 0x79, (byte) 0x9d,
			(byte) 0xdc, (byte) 0x80, (byte) 0xa9, (byte) 0x83, (byte) 0xf1,
			(byte) 0x03, (byte) 0xb6, (byte) 0x59, (byte) 0x83 };

	/**
	 * 加密
	 * 
	 * @param str
	 * @return
	 */
	public String encrypt(String str) {
		char[] rechar = str.toCharArray();
		for (int i = 0; i < rechar.length; i++) {
			rechar[i] = Character.reverseBytes(rechar[i]);
		}
		return new String(rechar);
	}

	/**
	 * 解密。
	 * 
	 * @param str
	 * @return
	 */
	public String decrypt(String str) {
		char[] rechar = str.toCharArray();
		for (int i = 0; i < rechar.length; i++) {
			rechar[i] = Character.reverseBytes(rechar[i]);
		}
		return new String(rechar);
	}

	/**
	 * 使用密码加密字符串。
	 * 
	 * @param str
	 * @param pass
	 * @return
	 */
	public String encrypt(String str, String pass) {
		try {
			IvParameterSpec ipSpec = new IvParameterSpec(initvector);
			// 负责保存对称密钥
			SecretKey key = new SecretKeySpec(
					genKeyFromPassword(pass), ALG_STRING);
			// 负责完成加密或解密工作
			Cipher cipher = Cipher.getInstance(ALG_STRING_CIPHER);
			cipher.init(Cipher.ENCRYPT_MODE, key, ipSpec);
			byte[] cipherStr = cipher.doFinal(str.getBytes("UTF-8"));
			return base64Encode(cipherStr);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用密码解密字符串。
	 * 
	 * @param str
	 * @param pass
	 * @return
	 */
	public String decrypt(String str, String pass) {
		try {
			IvParameterSpec ipSpec = new IvParameterSpec(initvector);
			SecretKey key = new SecretKeySpec(
				genKeyFromPassword(pass), ALG_STRING);
			Cipher cipher = Cipher.getInstance(ALG_STRING_CIPHER);
			cipher.init(Cipher.DECRYPT_MODE, key, ipSpec);
			byte[] strB = cipher.doFinal(base64Decode(str));
			return new String(strB, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * 采用Base64编码不仅比较简短，同时也具有不可读性
	 */
	private String base64Encode(byte[] bytes) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(bytes);
	}

	private byte[] base64Decode(
			String str) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(str);
	}

	private byte[] genKeyFromPassword(String str) {
		return md5sum(str.toString().getBytes());
	}

	/*
	 * 一个消息摘要就是一个数据块的数字指纹。MessageDigest提供了消息摘要算法，如 MD5。
	 * MD5是单向加密算法，它将任意大小的数据输出一个固定长度的散列值。
	 */
	private byte[] md5sum(byte[] buffer) {
		try {
			// 生成一个MessageDigest类,确定计算方法MD5
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// 消息
			md5.update(buffer);
			// 完成散列码的计算,返回存放散列值的字节数组
			return md5.digest();
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}

	/**
	 * 加密文件。
	 * 
	 * @param efile
	 * @param pass
	 * @return
	 * @throws Exception 
	 */
	public boolean encrypt(
			File efile, String pass) throws Exception {
		try {
			char[] buffer = new char[1024];
			InputStreamReader fileReader = new InputStreamReader(
					new FileInputStream(efile), "UTF-8");
			StringWriter swriter = new StringWriter();
			int re = -1;
			while ((re = fileReader.read(buffer)) != -1) {
				swriter.write(buffer, 0, re);
			}
			String str = swriter.toString();
			swriter.close();
			fileReader.close();
			String dstring = encrypt(str, pass);
			if (dstring == null) {
				return false;
			}
			OutputStreamWriter dfilewriter = new OutputStreamWriter(
					new FileOutputStream(efile), "UTF-8");
			dfilewriter.write(dstring);
			dfilewriter.close();
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

	/**
	 * 解密文件。
	 * 
	 * @param dfile
	 * @param pass
	 * @return
	 * @throws Exception 
	 */
	public boolean decrypt(
			File dfile, String pass) throws Exception {
		try {
			char[] buffer = new char[1024];
			InputStreamReader fileReader = new InputStreamReader(
					new FileInputStream(dfile), "UTF-8");
			StringWriter swriter = new StringWriter();
			int re = -1;
			while ((re = fileReader.read(buffer)) != -1) {
				swriter.write(buffer, 0, re);
			}
			String dstr = decrypt(swriter.toString(), pass);
			swriter.close();
			fileReader.close();
			if (dstr == null) {
				return false;
			}
			OutputStreamWriter dfilewriter = new OutputStreamWriter(
					new FileOutputStream(dfile), "UTF-8");
			dfilewriter.write(dstr);
			dfilewriter.close();
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

	/**
	 * 解密文件到内存中的字节数组。
	 * 
	 * @param dfile
	 * @param pass
	 * @return
	 * @throws Exception 
	 */
	public String decryptFileToString(
			File dfile, String pass) throws Exception {
		String restr = null;
		try {
			char[] buffer = new char[1024];
			InputStreamReader fileReader = new InputStreamReader(
					new FileInputStream(dfile), "UTF-8");
			StringWriter swriter = new StringWriter();
			int re = -1;
			while ((re = fileReader.read(buffer)) != -1) {
				swriter.write(buffer, 0, re);
			}
			restr = decrypt(swriter.toString(), pass);
			fileReader.close();
			swriter.close();
		} catch (Exception e) {
			throw e;
		}
		return restr;
	}
	
}
