package cn.sudt.lang.io;

import java.awt.Color;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.lang.StringUtils;
/**
 * 输出流重定向到JTextPane
 * 
 * @author chongming
 *
 */
public class PanelPrintStream extends PrintStream {
	private JTextPane textPane;
	/*
     * 字体颜色    
     */
	private Color foreground;

	public PanelPrintStream(OutputStream out, 
			JTextPane textPane, Color foreground) {
		 /*
		  * 使用自动刷新 
		  */
		super(out, true);
		this.foreground = foreground;
		this.out = out;
		this.textPane = textPane;
	}
	
	/**   
     * 所有的打印方法都要调用最底一层的方法   
     */  
	@Override
    public void write(final byte[] buf, final int off, final int len) { 
		try {
			String message = null;
			message = new String(Arrays.copyOfRange(
					buf, off, off + len));
			
			if (StringUtils.isNotBlank(message)) {
				StyledDocument doc = (StyledDocument) textPane.getDocument(); 
				
	            /*
	             *  创建和设置style对象   
	             */
	            Style style = doc.addStyle("StyleName", null); 
	            StyleConstants.setForeground(style, foreground); 
	            
	            doc.insertString(doc.getLength(), message.trim() + "\n", style);
	            /*
	             *  将光标定位到最低端
	             */
	            textPane.setCaretPosition(doc.getLength());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
    } 
       
}
