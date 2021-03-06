package cn.sudt.ui.dialog;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import cn.sudt.config.ToolConfig;
import cn.sudt.lang.swing.SwingFactory;
import cn.sudt.validate.annotation.Validations;
/**
 * 命令对话框模板
 *  
 * @author chongming
 *
 */

@SuppressWarnings("serial")
public abstract class AbstractCmdDialog extends AbstractDialog {
	
	@Validations(type=Validations.Type.text, regular = ".+", info = 
			"sudt.command.expression.null", getInfoLabel = "getCmdExpLabel")
	protected JTextField cmdExp = SwingFactory.getTextField();
	
	protected JLabel  cmdExpLabel = new JLabel(" " 
			+ ToolConfig.i18.getProperty("sudt.command.expression") + " ");
	
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
				"sudt.command.execute.delay"));
		JLabel secondLabel = new JLabel(ToolConfig.i18.getProperty(
				"sudt.unit.second"));
		
		JPanel delayExePanel = new JPanel();
		delayExePanel.add(delayExeLabel);
		delayExePanel.add(delay);
		delayExePanel.add(secondLabel);
		
		JLabel retryLabel = new JLabel(ToolConfig.i18.getProperty(
				"sudt.command.execute.retry"));
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
