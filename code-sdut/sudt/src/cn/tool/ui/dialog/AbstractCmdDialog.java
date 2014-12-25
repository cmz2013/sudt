package cn.tool.ui.dialog;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import cn.tool.config.ToolConfig;
import cn.tool.lang.swing.ComFactory;
import cn.tool.validate.annotation.Validations;
/**
 * 命令对话框模板
 *  
 * @author chongming
 *
 */

@SuppressWarnings("serial")
public abstract class AbstractCmdDialog extends AbstractDialog {
	
	@Validations(type=Validations.Type.text, regular = ".+", info = 
			"soft.deploy.tool.command.expression.null", getInfoLabel = "getCmdExpLabel")
	protected JTextField cmdExp = ComFactory.getTextField();
	
	protected JLabel  cmdExpLabel = new JLabel(" " 
			+ ToolConfig.i18.getProperty("soft.deploy.tool.command.expression") + " ");
	
	protected JSpinner delay = new JSpinner(new SpinnerNumberModel(
			0, 0, ToolConfig.deploy.getMaxDelay(), 1));
	protected JCheckBox retry = new JCheckBox();
	
	public AbstractCmdDialog(int dialogType, String titleKey) {
		super(dialogType);
		init();
		setLayout(titleKey);
	}

	private void init() {
		if (ToolConfig.deploy.isRetry()) {
			retry.setSelected(true);
		}
	}

	private void setLayout(String titleKey) {
		setTitle(ToolConfig.i18.getProperty(titleKey));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(cmdExp, BorderLayout.CENTER);
		mainPanel.add(cmdExpLabel, BorderLayout.WEST);
		
		JLabel delayExeLabel = new JLabel(ToolConfig.i18.getProperty(
				"soft.deploy.tool.command.execute.delay"));
		JLabel secondLabel = new JLabel(ToolConfig.i18.getProperty(
				"soft.deploy.tool.unit.second"));
		
		JPanel delayExePanel = new JPanel();
		delayExePanel.add(delayExeLabel);
		delayExePanel.add(delay);
		delayExePanel.add(secondLabel);
		
		JLabel retryLabel = new JLabel(ToolConfig.i18.getProperty(
				"soft.deploy.tool.command.execute.retry"));
		JPanel retryPanel = new JPanel();
		retryPanel.add(retryLabel);
		retryPanel.add(retry);
		
		JPanel exeSetPanel = new JPanel();
		exeSetPanel.add(retryPanel);
		exeSetPanel.add(delayExePanel);
		mainPanel.add(exeSetPanel, BorderLayout.SOUTH);
		
		add(mainPanel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	public JLabel getCmdExpLabel() {
		return cmdExpLabel;
	}
	
	@Override
	protected boolean validates() {
		return true;
	}
}
