package cn.sudt.lang.io;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextPane;

/**
 * 
 * 控制台流重定向
 * 
 * @author chongming
 * 
 */
public class ConsoleStdout {
	
	public void setOut(File file) throws IOException {
		PrintStream ps = new PrintStream(file);
		System.setOut(ps);
	}
	
	/**
	 * 
	 * @param textPane
	 * @param color: 输出时所用字体颜色 
	 */
	public void setOut(JTextPane textPane, Color color) {
		PanelPrintStream out = new PanelPrintStream(
				System.out, textPane, color);
		System.setOut(out);
	}
	
	public void setOut(OutputStream out) {
		//使用自动刷新 
		PrintStream ps = new PrintStream(out, true);
		System.setOut(ps);
	}
	
	public void setErr(File file) throws IOException {
		PrintStream ps = new PrintStream(file);
		System.setOut(ps);
	}
	
	public void setErr(JTextPane textPane, Color color) {
		PanelPrintStream out = new PanelPrintStream(
				System.out, textPane, color);
		System.setErr(out);
	}
	
	public void setErr(OutputStream out) {
		PrintStream ps = new PrintStream(out, true);
		System.setErr(ps);
	}
}