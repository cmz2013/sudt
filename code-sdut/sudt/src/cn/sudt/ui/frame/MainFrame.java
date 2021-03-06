package cn.sudt.ui.frame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import cn.sudt.config.ToolConfig;
import cn.sudt.ui.common.IconContainer;
import cn.sudt.ui.common.UIConst;
import cn.sudt.ui.tab.DeployTabbed;
import cn.sudt.ui.tab.HelpTabbed;
/**
 * 工具主窗口
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends AbstractFrame {
	
	public MainFrame() throws Exception {
		super();
		init();
		setFrameLayout();
		setVisible(true);
	}

	private void init() {
		setResizable(false);
		/*
		 * 通过监听器关闭窗口
		 */
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent e) { }
			public void windowIconified(WindowEvent e) { }
			public void windowDeiconified(WindowEvent e) { }
			public void windowDeactivated(WindowEvent e) { }
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(null, 
						ToolConfig.i18.getProperty("sudt.exit.confirm"),
						ToolConfig.i18.getProperty("sudt.title.confirm"),
						JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
			public void windowClosed(WindowEvent e) { }
			public void windowActivated(WindowEvent e) { }
		});
	}

	private void setBackgroundImage() {
		// 将背景图放在标签里
		JLabel bgImgLabel = new JLabel(IconContainer.icon_background);
		// 设置背景标签的位置
		bgImgLabel.setBounds(0, 0, IconContainer.icon_background.getIconWidth(),
				IconContainer.icon_background.getIconHeight());
		// 将背景标签添加到LayeredPane面板里
		getLayeredPane().add(bgImgLabel, new Integer(Integer.MIN_VALUE));
	}

	private void setFrameLayout() throws Exception {
		JTabbedPane mainPanel = new JTabbedPane();
		mainPanel.addTab(ToolConfig.i18.getProperty("sudt.deploy"), 
				new DeployTabbed().getDeployPanel());
		mainPanel.addTab(ToolConfig.i18.getProperty("sudt.help.tip"), 
				new HelpTabbed().getHelpPane());
		setContentPane(mainPanel);
		setLocation(UIConst.FRAME_X, UIConst.FRAME_Y);
		setBackgroundImage();
	}

}
