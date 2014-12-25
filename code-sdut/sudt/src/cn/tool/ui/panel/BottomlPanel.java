package cn.tool.ui.panel;

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

import cn.tool.config.ToolConfig;
import cn.tool.lang.swing.ComFactory;
import cn.tool.shell.LocalShellRunner;
import cn.tool.ui.resour.IconContainer;
import cn.tool.ui.resour.SystemConst;
/**
 * 窗口底层布局
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class BottomlPanel extends JPanel {
	/**
	 * 日志文件夹按钮
	 */
	private JMenuItem logView = ComFactory.getMenuItem(
			"soft.deploy.tool.log", IconContainer.icon_log);
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
				
					LocalShellRunner.execute(
							"start " + ToolConfig.LOG_DIR);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, 
						ex.getMessage(),
						ToolConfig.i18.getProperty("soft.deploy.tool.error"), 
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
		setPreferredSize(new Dimension(SystemConst.FRAME_WIDTH, 16));
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