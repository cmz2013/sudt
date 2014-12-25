package cn.tool.ui.frame;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import cn.tool.config.ToolConfig;
import cn.tool.lang.io.ConsoleStdout;
import cn.tool.ui.resour.SystemConst;

/**
 * 控制台（单例模式）
 * 
 * @author chongming
 *
 */

/*
 * 管道把一个线程的输出连通到另一个线程的输入。 管道输出流作为管道的发送端，
 * 与管道的接收端(管道输入流)配合， 把数据送到输入流中。创建 PipedOutputStream
 * 类的对象： PipedOutputStream pos = new PipedOutputStream;
 * 假如有个管道输入流对象pis， 可以用： pos.connect(pis)；将输入输出流连接起来，
 * 也可以直接用下式连接： PipedOutputStream pos = new PipedOutputStream(pis)；
 * 
 * 在利用管道读写数据时，必须保证利用管道读写数据的线程都不能退出，否则会抛出异常：
 * IOException: Write end dead。(该工具采用多个并发线程进行部署，
 * 如果利用管道实现控制台输出重定向，繁琐、该BUG必现，下面采用更简洁高效的实现:
 * ConsoleStdout.setOut(textPane, Color.BLACK);
 * ConsoleStdout.setErr(textPane, Color.RED);)
 */
@SuppressWarnings("serial")
public class ConsoleFrame extends AbstractFrame {
	/**
	 * 单例模式
	 */
	private static ConsoleFrame console = null;
	
	private JTextPane textPane = new JTextPane();
	
	private ConsoleFrame() throws IOException {
		super();
		init();
		setLayout();
		setVisible(true);
	}
	
	private void init() {
		setTitle(getTitle() + " - " + ToolConfig.i18.getProperty(
				"soft.deploy.tool.console"));
		ConsoleStdout.setOut(textPane, Color.BLACK);
		ConsoleStdout.setErr(textPane, Color.RED);
	}

	private void setLayout() throws IOException  {
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane);
		setLocation(SystemConst.FRAME_X + 
				SystemConst.FRAME_WIDTH, SystemConst.FRAME_Y);
	}

	public static ConsoleFrame showConsole() {
		try {
			if (null == console) {
				console = new ConsoleFrame();
			} else {
				console.textPane.setText("");
				console.setLocation(
						SystemConst.FRAME_X + SystemConst.FRAME_WIDTH,
						SystemConst.FRAME_Y);
				console.setSize(SystemConst.FRAME_WIDTH, SystemConst.FRAME_HEIGHT);
				if (!console.isVisible()) {
					console.setVisible(true);
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, 
					e.getMessage(), 
					ToolConfig.i18.getProperty("soft.deploy.tool.error"), 
					JOptionPane.ERROR_MESSAGE);
		}
		return console;
	}

}
