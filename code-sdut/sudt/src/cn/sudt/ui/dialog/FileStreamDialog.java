package cn.sudt.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import cn.sudt.config.ToolConfig;
import cn.sudt.lang.swing.SwingFactory;
import cn.sudt.ui.common.UIConst;
import cn.sudt.validate.annotation.Validations;
/**
* 文件导入导出对话框模板
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public abstract class FileStreamDialog extends AbstractDialog {
	
	public static final String FILE_SUFFIX_CFG = ".cfg";
	
	private JLabel pasLabel = new JLabel(" " + ToolConfig.i18.getProperty(
			"sudt.password.input") + " ");
	private JLabel fileLabel = new JLabel(" " + ToolConfig.i18.getProperty(
			"sudt.file.path") + " ");
	@Validations(type=Validations.Type.text, regular = ".+", info = 
			"sudt.file.path.null", getInfoLabel = "getFileLabel")
	protected JTextField filePathField = SwingFactory.getTextField();
	
	@Validations(type=Validations.Type.text, regular = ".+", info = 
			"sudt.password.null", getInfoLabel = "getPasLabel")
	protected JTextComponent passwordField;
	
	protected JButton browseBtn = SwingFactory.getButton(
			"sudt.file.browse");
	
	protected JCheckBox encryptCheckBox = new JCheckBox();
	protected JPanel btnPanel = new JPanel();
	protected JPanel contextPanel = new JPanel();
	protected JPanel labelPanel = new JPanel();
	
	private JFileChooser fileChooser = SwingFactory.getFileChooser();
	
	public FileStreamDialog(int dialogType) {
		super(dialogType);
		init();
		setListener();
		setLayout();
	}
	
	protected void init() {
		filePathField.setColumns(UIConst.TEXT_FIELD_COLUMNS - 6);
		if (ToolConfig.user.isShowPassword()) {
			passwordField = SwingFactory.getTextField();
			if (passwordField instanceof JTextField) {
				JTextField textField = (JTextField) passwordField;
				textField.setColumns(UIConst.TEXT_FIELD_COLUMNS - 6);
			}
		} else {
			passwordField = new JPasswordField(UIConst.TEXT_FIELD_COLUMNS - 6);
		}
		filePathField.setEditable(false);
	}
	
	public JLabel getPasLabel() {
		return pasLabel;
	}

	public JLabel getFileLabel() {
		return fileLabel;
	}
	
	@Override
	protected void clearContext() {
		filePathField.setText("");
		encryptCheckBox.setSelected(false);
		setDefaultPassword();
		/*
		 * 浏览按钮默认聚焦 
		 */
		browseBtn.requestFocus();
	}

	protected void setDefaultPassword() {
		setPasswordEnabled(false);
		passwordField.setText("");
	}
	
	protected void setPasswordEnabled(boolean status) {
		pasLabel.setEnabled(status);
		passwordField.setEnabled(status);
	}
	
	protected void setLayout() {
		setPasswordEnabled(false);
		fileLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		pasLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel encrypLabel = new JLabel(" " + ToolConfig.i18.getProperty(
				"sudt.file.encryption") + " ");
		encrypLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel checkBoxLabel = new JLabel(ToolConfig.i18.getProperty(
				"sudt.file.password.tip"));
		checkBoxLabel.setForeground(Color.RED);
		
		JPanel checkBoxPanel = new JPanel(new BorderLayout());
		checkBoxPanel.add(encryptCheckBox, BorderLayout.WEST);
		checkBoxPanel.add(checkBoxLabel, BorderLayout.CENTER);
		
		labelPanel.add(fileLabel);
		labelPanel.add(encrypLabel);
		labelPanel.add(pasLabel);
		
		contextPanel.add(filePathField);
		contextPanel.add(checkBoxPanel);
		contextPanel.add(passwordField);
		
		btnPanel.add(browseBtn);
		
		JPanel importPanel = new JPanel();
		importPanel.setLayout(new BorderLayout());
		importPanel.add(contextPanel, BorderLayout.CENTER);
		importPanel.add(labelPanel, BorderLayout.WEST);
		importPanel.add(btnPanel, BorderLayout.EAST);
		
		add(importPanel, BorderLayout.CENTER);
		pack();
	}
	
	private void setPasswordCheckBoxActionListener() {
		encryptCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (encryptCheckBox.isSelected()) {
						setPasswordEnabled(true);
						passwordField.requestFocus();
					} else {
						setDefaultPassword();
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), 
							ToolConfig.i18.getProperty("sudt.title.error"),
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}
	
	private void setListener() {
		setPasswordCheckBoxActionListener();
		setBrowseBtnKeyListener();
		setBrowseBtnActionListener();
	}
	
	private void setBrowseBtnKeyListener() {
		browseBtn.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) { }
			public void keyReleased(KeyEvent e) {
				if ("Enter".equalsIgnoreCase(
					KeyEvent.getKeyText(e.getKeyCode()))) {
					browseBtn.doClick();
				}
			}
			public void keyPressed(KeyEvent e) { }
		});
	}

	private void setBrowseBtnActionListener() {
		browseBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				int option = JFileChooser.CANCEL_OPTION;
				if (UIConst.DIALOG_IMPORT == getDialogType()) {
					option = fileChooser.showOpenDialog(getDialog());
				} else {
					option = fileChooser.showSaveDialog(getDialog());
				}
				
				if (JFileChooser.CANCEL_OPTION != option) {
					String filePath = fileChooser.getSelectedFile(
							).getAbsolutePath().trim();
					if (!filePath.toLowerCase().endsWith(FILE_SUFFIX_CFG)) {
						filePathField.setText(filePath + FILE_SUFFIX_CFG);
					} else {
						filePathField.setText(filePath);
					}
				}
			}
		});
	}

}
