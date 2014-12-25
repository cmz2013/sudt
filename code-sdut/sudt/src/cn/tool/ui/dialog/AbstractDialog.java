package cn.tool.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;

import cn.tool.config.ToolConfig;
import cn.tool.lang.swing.ComFactory;
import cn.tool.ui.resour.IconContainer;
import cn.tool.ui.resour.SystemConst;
import cn.tool.validate.ValidateUtils;
import cn.tool.validate.annotation.Validations;
import cn.tool.validate.model.DataModel;
/**
 * 对话框模板
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractDialog extends JDialog {
	
	private JButton sureBtn = ComFactory.getButton("soft.deploy.tool.button.sure");
	private JButton cancelBtn = ComFactory.getButton("soft.deploy.tool.button.cancel");
	private JLabel infoLabel = new JLabel(" ");
	
	private Map<JLabel, String> labelMap = new HashMap<JLabel, String>();
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

	/**
	 * 封装数据模型DataModel
	 * 
	 * @throws Exception
	 */
	private List<DataModel> getDataModels() throws Exception {
		List<DataModel> dataModels = new ArrayList<>();
		
		Class<?> clazz  = null;
		do {
			if (null == clazz) {
				clazz = getDialog().getClass();
			} else {
				clazz = clazz.getSuperclass();
			}
			
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(Validations.class)) {
					field.setAccessible(true);
					Validations valaAnno = field.getAnnotation(Validations.class);
					Object dataObj =  field.get(getDialog());
					
					if (null != dataObj) {
						if (dataObj instanceof JTextComponent) {
							JTextComponent textBox = (JTextComponent) dataObj;
							DataModel model = getTextBoxDataModels(textBox, valaAnno);
							if (null != model) {
								dataModels.add(model);
							}
						}
					}
					field.setAccessible(false);
				}
			}		
		} while(!AbstractDialog.class.equals(clazz));
		
		return dataModels;
	}

	/**
	 * JTextComponent类型的 DataModels
	 * @param textBox
	 * @param valaAnno
	 * @throws Exception
	 */
	private DataModel getTextBoxDataModels(JTextComponent
			textBox, Validations valaAnno) throws Exception {
		
		boolean enabled = textBox.isEnabled();
		/*
		 * 组件不可用时不验证
		 */
		if (enabled) {
			JLabel label = (JLabel) getDialog().getClass().getMethod(
					valaAnno.getInfoLabel()).invoke(getDialog());
			
			if (!labelMap.containsKey(label)) {
				labelMap.put(label, label.getText());
				ComFactory.setMouseListener(textBox, label, labelMap.get(label));
			}
			
			DataModel dataModel = new DataModel();
			dataModel.setInfoLabel(label);
			dataModel.setType(valaAnno.type());
			dataModel.setValue(textBox.getText());
			dataModel.setInfo(ToolConfig.i18.getProperty(valaAnno.info()));
			dataModel.setRegular(valaAnno.regular());
			return dataModel;
		}
		return null;
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
					List<DataModel> dataModels = getDataModels();
					if(ValidateUtils.validate(dataModels) && validates()) {
						String mes = sureExecute();
						// handle success
						setSuccessInfo(mes);
					}
				} catch (Exception e) {
					// handle exception
					setFailureInfo();
					JOptionPane.showMessageDialog(null, e.getMessage(),
							ToolConfig.i18.getProperty("soft.deploy.tool.error"),
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	private void setFailureInfo() {
		if (SystemConst.DIALOG_ADD == dialogType) {
			showInfo(IconContainer.icon_error, Color.RED, 
					ToolConfig.i18.getProperty("soft.deploy.tool.add.fail"));
		} else if (SystemConst.DIALOG_UPDATE == dialogType) {
			showInfo(IconContainer.icon_error, Color.RED, 
					ToolConfig.i18.getProperty("soft.deploy.tool.update.fail"));
		} else if (SystemConst.DIALOG_IMPORT == dialogType) {
			showInfo(IconContainer.icon_error, Color.RED, 
					ToolConfig.i18.getProperty("soft.deploy.tool.import.fail"));
		} else if (SystemConst.DIALOG_EXPORT == dialogType) {
			showInfo(IconContainer.icon_error, Color.RED, 
					ToolConfig.i18.getProperty("soft.deploy.tool.export.fail"));
		}
	}

	private void setSuccessInfo(String mes) {
		if (StringUtils.isBlank(mes)) {
			mes = "";
		}
		
		Color color = new Color(0,100,0);
		if (SystemConst.DIALOG_ADD == dialogType) {
			showInfo(IconContainer.icon_right, color, 
					ToolConfig.i18.getProperty("soft.deploy.tool.add.success") + mes);
		} else if (SystemConst.DIALOG_UPDATE == dialogType) {
			showInfo(IconContainer.icon_right, color, 
					ToolConfig.i18.getProperty("soft.deploy.tool.update.success") + mes);
		} else if (SystemConst.DIALOG_IMPORT == dialogType) {
			showInfo(IconContainer.icon_right, color, 
					ToolConfig.i18.getProperty("soft.deploy.tool.import.success") + mes);
		} else if (SystemConst.DIALOG_EXPORT == dialogType) {
			showInfo(IconContainer.icon_right, color, 
					ToolConfig.i18.getProperty("soft.deploy.tool.export.success") + mes);
		}
	}
	
	/**
	 * 显示提示信息
	 * 
	 * @param icon
	 * @param foreground
	 * @param info
	 */
	protected void showInfo(Icon icon, final Color foreground, String info) {
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
				if (SystemConst.DIALOG_ADD == dialogType && 
						color.equals(infoLabel.getForeground())) {
					
					if (ToolConfig.user.isCloseAdd()) {
						closeDialog();
					} else {
						clearContext();
					}
				} else if ((SystemConst.DIALOG_IMPORT == dialogType
						|| SystemConst.DIALOG_EXPORT == dialogType) && 
						color.equals(infoLabel.getForeground())) {
					
					closeDialog();
				} else if ((SystemConst.DIALOG_UPDATE == dialogType) && 
						color.equals(infoLabel.getForeground()) &&
					ToolConfig.user.isCloseUpdate()) {
					closeDialog();
				}
				sureBtn.setEnabled(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), 
						ToolConfig.i18.getProperty("soft.deploy.tool.error"), 
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

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
	
	/**
	 * 获取实例
	 * @return
	 */
	protected abstract JDialog getDialog();

	private void setCancelBtnActionListener() {
		cancelBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeDialog();
				setLabelDefaultText();
			}
		});
	}

	private void setLayout() {
		setIconImage((IconContainer.icon_item.getImage()));
		setLocation(SystemConst.FRAME_X + SystemConst.FRAME_WIDTH, 
				SystemConst.FRAME_Y);
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
				setLocation(SystemConst.FRAME_X + 
						SystemConst.FRAME_WIDTH, SystemConst.FRAME_Y);
			}
		}).start();
	}

	/**
	 * 清空验证失败提示信息
	 */
	private void setLabelDefaultText() {
		for (JLabel label : labelMap.keySet()) {
			if (Color.RED.equals(label.getForeground())) {
				label.setText(labelMap.get(label));
				label.setForeground(Color.BLACK);
			}
		}
	}
	
}