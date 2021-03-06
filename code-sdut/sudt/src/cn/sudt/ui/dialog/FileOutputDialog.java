package cn.sudt.ui.dialog;

import java.awt.GridLayout;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import cn.sudt.config.ToolConfig;
import cn.sudt.lang.io.CryptoUtils;
import cn.sudt.lang.io.ObjectStream;
import cn.sudt.lang.io.StringStream;
import cn.sudt.ui.common.MessageType;
import cn.sudt.ui.common.UIConst;
import cn.sudt.validate.annotation.Validations;
/**
* 文件导出对话框模板
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public abstract class FileOutputDialog extends FileStreamDialog {

	private JLabel sureLabel;
	
	private List<?> objs;
	
	@Validations(type=Validations.Type.text, regular = ".+", info = 
		"sudt.password.sure.null", getInfoLabel = "getSureLabel")
	protected JTextComponent sureField;
	
	public FileOutputDialog(List<?> objs) {
		super(UIConst.DIALOG_EXPORT);
		this.objs = objs;
	}
	
	@Override
	protected void init() {
		super.init();
		if (!ToolConfig.user.isShowPassword()) {
			sureLabel = new JLabel(" " + ToolConfig.i18.getProperty(
					"sudt.password.sure") + " ");
			sureField = new JPasswordField(UIConst.TEXT_FIELD_COLUMNS - 6);
		}
	}

	@Override
	protected void setDefaultPassword() {
		super.setDefaultPassword();
		if (!ToolConfig.user.isShowPassword()) {
			sureField.setText("");
		}
	}
	
	@Override
	protected void setPasswordEnabled(boolean status) {
		super.setPasswordEnabled(status);
		if (!ToolConfig.user.isShowPassword()) {
			sureField.setEnabled(status);
			sureLabel.setEnabled(status);
		}
	}

	@Override
	protected void setLayout() {
		if (!ToolConfig.user.isShowPassword()) {
			labelPanel.setLayout(new GridLayout(4, 1));
			contextPanel.setLayout(new GridLayout(4, 1));
			btnPanel.setLayout(new GridLayout(4, 1));
			super.setLayout();
			sureLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			labelPanel.add(sureLabel);
			contextPanel.add(sureField);
		} else {
			labelPanel.setLayout(new GridLayout(3, 1));
			contextPanel.setLayout(new GridLayout(3, 1));
			btnPanel.setLayout(new GridLayout(3, 1));
			super.setLayout();
		}
	}
	
	@Override
	protected boolean validates() {
		
		if (!ToolConfig.user.isShowPassword() 
				&& encryptCheckBox.isSelected()) {
			
			if (!passwordField.getText().equals(sureField.getText())) {
				showInfo(MessageType.error, ToolConfig.
					i18.getProperty("sudt.password.sure.error"));
				return false;
			}
			
		}
		
		if (new File(filePathField.getText()).exists()) {
			int option = JOptionPane.showConfirmDialog(null, 
				String.format(ToolConfig.i18.getProperty(
					"sudt.file.exist"), 
					filePathField.getText()), 
				ToolConfig.i18.getProperty("sudt.title.confirm"), 
				JOptionPane.WARNING_MESSAGE);
			if (JOptionPane.CANCEL_OPTION == option) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected String sureExecute() throws Exception {
		File file = new File(filePathField.getText());
		if (encryptCheckBox.isSelected()) {
			String encStr = new CryptoUtils().encrypt(
				originalString(), passwordField.getText());
			
			new StringStream().writeString(encStr, file);
		} else {
			new ObjectStream().writeObject(file, objs.toArray());
		}
		return null;
	}
	
	/**
	 * 为了不加密时读写文件方便简单，可以直接用对象流
	 * 
	 * (对象流文件打开乱码，不能正常加密解密, 故重写toString)
	 * 
	 * @return
	 */
	private String originalString() {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < objs.size() - 1; i++) {
			Object obj = objs.get(i);
			strBuf.append(obj.toString() + "\r\n");
		}
		strBuf.append(objs.get(objs.size() - 1).toString());
		return strBuf.toString();
	}

	public JLabel getSureLabel() {
		return sureLabel;
	}
	
}
