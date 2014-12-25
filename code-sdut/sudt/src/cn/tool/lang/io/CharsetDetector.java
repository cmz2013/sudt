package cn.tool.lang.io;

import java.io.UnsupportedEncodingException;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;
/**
 *  编码检测
 * 
 * @author chongming
 *
 */
public class CharsetDetector {

	/**
	 * 解析字节数组编码,返回字符串。如果无法解析,则按照UTF-8编码格式返回
	 * 
	 * 注意：返回结果可能为乱码
	 */
	public static String detect(byte[] buf)
			throws UnsupportedEncodingException {
		//language用以提示语言
		int language = nsPSMDetector.ALL;
		nsDetector detector = new nsDetector(language);
		
		//检查是否为ascii字符，当有一个字符不是，则所有的数据即不是ASCII编码了
		boolean asciiCode = true;
		asciiCode = detector.isAscii(buf, buf.length);
		if (asciiCode) {
			return new String(buf, "ascii");
		}
		
		CharsetDetectionObserver cdo = new CharsetDetectionObserver(buf);
		detector.Init(cdo);
		detector.DoIt(buf, buf.length, false);

		//最后要调用此方法，此时，Notify被调用
		detector.DataEnd();
		String res = cdo.toString();
		if (null != res) {
			return res;
		}
		return new String(buf, "UTF-8");
	}
}

class CharsetDetectionObserver implements nsICharsetDetectionObserver {
	private String charset = null;
	private byte[] buffer = null;

	CharsetDetectionObserver(byte[] buf) {
		this.buffer = buf;
	}

	/**
	 * 当chardet引擎自己认为已经识别出字符串的字符集后(不论识别的对错)，都会调用这个方法
	 */
	public void Notify(String charset) {
		this.charset = charset;
	}

	@Override
	public String toString() {
		if (this.charset != null && buffer != null) {
			String s = null;
			try {
				s = new String(buffer, this.charset);
			} catch (UnsupportedEncodingException e) {
				
			}
			return s;
		}
		return null;
	}

	public String getCharset() {
		return charset;
	}

}
