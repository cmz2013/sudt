package cn.tool.ui.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import cn.tool.config.ToolConfig;
import cn.tool.lang.swing.ComFactory;
import cn.tool.validate.annotation.Validations;
/**
 * 主机对话框模板
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractHostDialog extends AbstractDialog {
	protected JLabel pasLabel = new JLabel(" "
			+ ToolConfig.i18.getProperty("soft.deploy.tool.password") + " ");
	protected JLabel ipLabel = new JLabel(" "
			+ ToolConfig.i18.getProperty("soft.deploy.tool.host.ip") + " ");
	protected JLabel userLabel = new JLabel(" "
			+ ToolConfig.i18.getProperty("soft.deploy.tool.host.user") + " ");
	
	@Validations(type=Validations.Type.text, regular = ".+", info = 
			"soft.deploy.tool.password.null", getInfoLabel = "getPasLabel")
	protected JTextField pasField;
	
	@Validations(type=Validations.Type.ip, getInfoLabel = "getIpLabel")
	protected JTextField ipField = ComFactory.getTextField();
	
	@Validations(type=Validations.Type.text, regular = ".+", info = 
			"soft.deploy.tool.host.user.null", getInfoLabel = "getUserLabel")
	protected JTextField userField = ComFactory.getTextField();

	protected JPanel mainPanel = new JPanel();
	protected JPanel fieldPanel = new JPanel();
	protected JPanel labelPanel = new JPanel();
	
	protected JTable hostList;
	
	public AbstractHostDialog(int dialogType, String titleKey, JTable hostList) {
		super(dialogType);
		this.hostList = hostList;
		if (ToolConfig.user.isShowPassword()) {
			pasField = ComFactory.getTextField();
		} else {
			pasField = new JPasswordField(26);
		}
		setLayout(titleKey);
	}
	
	public JLabel getPasLabel() {
		return pasLabel;
	}

	public JLabel getIpLabel() {
		return ipLabel;
	}

	public JLabel getUserLabel() {
		return userLabel;
	}
	
	private void setLayout(String titleKey) {
		pasLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		ipLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		setTitle(ToolConfig.i18.getProperty(titleKey));
		labelPanel.setLayout(new GridLayout(3, 1));
		fieldPanel.setLayout(new GridLayout(3, 1));
		labelPanel.add(ipLabel);
		labelPanel.add(userLabel);
		labelPanel.add(pasLabel);
		fieldPanel.add(ipField);
		fieldPanel.add(userField);
		fieldPanel.add(pasField);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(labelPanel, BorderLayout.WEST);
		mainPanel.add(fieldPanel, BorderLayout.CENTER);
		add(mainPanel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	
}