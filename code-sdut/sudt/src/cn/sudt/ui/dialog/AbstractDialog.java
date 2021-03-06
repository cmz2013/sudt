package cn.sudt.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import cn.sudt.config.ToolConfig;
import cn.sudt.lang.swing.SwingFactory;
import cn.sudt.ui.common.IconContainer;
import cn.sudt.ui.common.MessageType;
import cn.sudt.ui.common.UIConst;
import cn.sudt.validate.ValidateService;
/**
 * 对话框模板
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractDialog extends JDialog {
	
	private JButton sureBtn = SwingFactory.getButton("sudt.button.sure");
	private JButton cancelBtn = SwingFactory.getButton("sudt.button.cancel");
	private JLabel infoLabel = new JLabel(" ");
	
	private ValidateService valService = new ValidateService();
	private int dialogType;

	public AbstractDialog(int dialogType) {
		super();
		this.dialogType = dialogType;
		init();
		setLayout();
	}

	private void init() {
		setCancelBtnActionListener();
		setSureBtnActionListener();
		setWindowListener();
	}

	public int getDialogType() {
		return dialogType;
	}

	public void setDialogType(int dialogType) {
		this.dialogType = dialogType;
	}

	private void setWindowListener() {
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				cancelBtn.doClick();
			}
			
			public void windowOpened(WindowEvent arg0) { }
			public void windowIconified(WindowEvent arg0) { }
			public void windowDeiconified(WindowEvent arg0) { }
			public void windowDeactivated(WindowEvent arg0) { }
			public void windowClosed(WindowEvent arg0) { }
			public void windowActivated(WindowEvent arg0) { }
		});
	}

	private void setSureBtnActionListener() {
		sureBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(valService.validate(getDialog()) && validates()) {
						String mes = sureExecute();
						// handle success
						setSuccessInfo(mes);
					}
				} catch (Exception e) {
					// handle exception
					setFailureInfo();
					JOptionPane.showMessageDialog(null, e.getMessage(),
							ToolConfig.i18.getProperty("sudt.title.error"),
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	private void setFailureInfo() {
		if (UIConst.DIALOG_ADD == dialogType) {
			showInfo(MessageType.error, 
					ToolConfig.i18.getProperty("sudt.add.fail"));
		} else if (UIConst.DIALOG_UPDATE == dialogType) {
			showInfo(MessageType.error, 
					ToolConfig.i18.getProperty("sudt.update.fail"));
		} else if (UIConst.DIALOG_IMPORT == dialogType) {
			showInfo(MessageType.error, 
					ToolConfig.i18.getProperty("sudt.import.fail"));
		} else if (UIConst.DIALOG_EXPORT == dialogType) {
			showInfo(MessageType.error, 
					ToolConfig.i18.getProperty("sudt.export.fail"));
		}
	}

	private void setSuccessInfo(String mes) {
		if (StringUtils.isBlank(mes)) {
			mes = "";
		}
		
		if (UIConst.DIALOG_ADD == dialogType) {
			showInfo(MessageType.right, 
					ToolConfig.i18.getProperty("sudt.add.success") + mes);
		} else if (UIConst.DIALOG_UPDATE == dialogType) {
			showInfo(MessageType.right, 
					ToolConfig.i18.getProperty("sudt.update.success") + mes);
		} else if (UIConst.DIALOG_IMPORT == dialogType) {
			showInfo(MessageType.right, 
					ToolConfig.i18.getProperty("sudt.import.success") + mes);
		} else if (UIConst.DIALOG_EXPORT == dialogType) {
			showInfo(MessageType.right, 
					ToolConfig.i18.getProperty("sudt.export.success") + mes);
		}
	}
	
	/**
	 * 显示提示信息
	 * 
	 * @param type
	 * @param info
	 */
	protected void showInfo(MessageType type, String info) {
		Icon icon = null;
		Color foreground = null;
		
		if (MessageType.error.equals(type)) {
			icon = IconContainer.icon_error;
			foreground = Color.RED;
		} else if (MessageType.right.equals(type)) {
			icon = IconContainer.icon_right;
			foreground = new Color(0,100,0);
		}
		
		sureBtn.setEnabled(false);
		if (null != icon) {
			infoLabel.setIcon(icon);
		}
		if (StringUtils.isNotBlank(info)) {
			infoLabel.setText(info);
		}
		if (null != foreground) {
			infoLabel.setForeground(foreground);
		}
		Thread infoClearThread = new InfoClearThread();
		infoClearThread.start();
	}
	
	/**
	 * 1.5秒之后清空提示信息
	 */
	private class InfoClearThread extends Thread {
		
		@Override
		public void run() {
			try {
				Thread.sleep(1500);
				infoLabel.setText(" ");
				infoLabel.setIcon(null);
				Color color = new Color(0,100,0);
				if (UIConst.DIALOG_ADD == dialogType && 
						color.equals(infoLabel.getForeground())) {
					
					if (ToolConfig.user.isCloseAdd()) {
						closeDialog();
					} else {
						clearContext();
					}
				} else if ((UIConst.DIALOG_IMPORT == dialogType
						|| UIConst.DIALOG_EXPORT == dialogType) && 
						color.equals(infoLabel.getForeground())) {
					
					closeDialog();
				} else if ((UIConst.DIALOG_UPDATE == dialogType) && 
						color.equals(infoLabel.getForeground()) &&
					ToolConfig.user.isCloseUpdate()) {
					closeDialog();
				}
				sureBtn.setEnabled(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), 
						ToolConfig.i18.getProperty("sudt.title.error"), 
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public abstract AbstractDialog getDialog();

	/**
	 * 清空输入组件中的内容，或设为默认值
	 * @param comps
	 */
	protected abstract void clearContext();
	
	/**
	 * 在确定按钮响应事件之前进行数据验证
	 */
	protected abstract boolean validates();
	
	/**
	 * 确定按钮响应事件
	 * 
	 * @throws Exception
	 */
	protected abstract String sureExecute() throws Exception;
	
	private void setCancelBtnActionListener() {
		cancelBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeDialog();
				valService.setLabelDefaultText();
			}
		});
	}

	private void setLayout() {
		setIconImage((IconContainer.icon_theme.getImage()));
		setLocation(UIConst.FRAME_X + UIConst.FRAME_WIDTH, 
				UIConst.FRAME_Y);
		setLayout(new BorderLayout());
		
		JPanel btnPanel = new JPanel();
		btnPanel.add(sureBtn);
		btnPanel.add(cancelBtn);
		add(btnPanel, BorderLayout.SOUTH);
		add(infoLabel, BorderLayout.NORTH);
		setResizable(false);
	}

	private void closeDialog() {
		setVisible(false);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(500);
					clearContext();
				} catch (Exception e) {
					
				}
				setLocation(UIConst.FRAME_X + 
						UIConst.FRAME_WIDTH, UIConst.FRAME_Y);
			}
		}).start();
	}

}
