package cn.tool.ui.dialog;

import javax.swing.JDialog;

import cn.tool.config.ToolConfig;
import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.panel.CmdTablePanel;
import cn.tool.ui.resour.SystemConst;
/**
 * 添加命令对话框
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class CmdAddDialog extends AbstractCmdDialog {
	private CmdTablePanel cmdPanel;
	
	public CmdAddDialog(CmdTablePanel cmdPanel) {
		super(SystemConst.DIALOG_ADD,
			"soft.deploy.tool.command.list.add");
		this.cmdPanel = cmdPanel;
	}

	@Override
	public void clearContext() {
		cmdExp.setText("");
		delay.setValue(0);
		retry.setSelected(ToolConfig.deploy.isRetry());
		cmdExp.requestFocus();
	}
	
	@Override
	protected JDialog getDialog() {
		return this;
	}

	@Override
	protected String sureExecute() throws Exception {
		TableUtils.appendRow(cmdPanel.getTable(), 
				cmdExp.getText(), 
				retry.isSelected(),
				delay.getValue());
		
		cmdPanel.getCmdExportBtn().setEnabled(true);
		return null;
	}
}