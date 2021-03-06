package cn.sudt.ui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cn.sudt.config.ToolConfig;
import cn.sudt.lang.swing.SwingFactory;
import cn.sudt.shell.LocalShellExcutor;
import cn.sudt.ui.common.IconContainer;
import cn.sudt.ui.common.UIConst;
/**
 * 部署窗口底层布局面板
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class BottomlPanel extends JPanel {
	/**
	 * 日志文件夹按钮
	 */
	private JMenuItem logView = SwingFactory.getMenuItem(
			"sudt.log", IconContainer.icon_log);
	/**
	 * 任务栏标签
	 */
	private JLabel busyLabel = new JLabel();
	private JLabel busyIconLabel = new JLabel();
	
	public BottomlPanel() {
		super();
		init();
		setLayout();
		setListener();
	}
	
	private void init() {
		busyIconLabel.setIcon(IconContainer.icon_busy);
		busyIconLabel.setEnabled(false);
	}

	private void setListener() {
		logView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File logFile = new File(ToolConfig.LOG_DIR);
					if (!logFile.exists()) {
						logFile.mkdirs();
					}
				
					new LocalShellExcutor().execute(
							"start " + ToolConfig.LOG_DIR);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, 
						ex.getMessage(),
						ToolConfig.i18.getProperty("sudt.title.error"), 
						JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private void setLayout() {
		setLayout(new BorderLayout());
		add(busyIconLabel, BorderLayout.WEST);
		busyLabel.setVerticalAlignment(SwingConstants.TOP);
		add(busyLabel, BorderLayout.CENTER);
		add(logView, BorderLayout.EAST);
		setPreferredSize(new Dimension(UIConst.FRAME_WIDTH, 16));
	}
	
	public JMenuItem getLogView() {
		return logView;
	}
	
	public void setLogView(JMenuItem logView) {
		this.logView = logView;
	}
	
	public JLabel getBusyLabel() {
		return busyLabel;
	}
	
	public void setBusyLabel(JLabel busyLabel) {
		this.busyLabel = busyLabel;
	}

	public JLabel getBusyIconLabel() {
		return busyIconLabel;
	}

	public void setBusyLabelText(String text) {
		busyLabel.setText(text);
	}
	
}
