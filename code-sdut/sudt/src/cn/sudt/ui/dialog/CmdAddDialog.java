package cn.sudt.ui.dialog;

import cn.sudt.config.ToolConfig;
import cn.sudt.ui.common.UIConst;
import cn.sudt.ui.panel.CmdTablePanel;
/**
 * 添加命令对话框
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class CmdAddDialog extends AbstractCmdDialog {
	private CmdTablePanel cmdTablePanel;
	
	public CmdAddDialog(CmdTablePanel cmdTablePanel) {
		super(UIConst.DIALOG_ADD,
			"sudt.command.list.add");
		this.cmdTablePanel = cmdTablePanel;
	}

	@Override
	public void clearContext() {
		cmdExp.setText("");
		delay.setValue(0);
		retry.setSelected(ToolConfig.deploy.isRetry());
		cmdExp.requestFocus();
	}
	
	@Override
	public AbstractDialog getDialog() {
		return this;
	}

	@Override
	protected String sureExecute() throws Exception {
		cmdTablePanel.getTableUtils().appendRow(
				cmdTablePanel.getTable(), 
				cmdExp.getText(), 
				retry.isSelected(),
				delay.getValue());
		
		cmdTablePanel.getCmdExportBtn().setEnabled(true);
		return null;
	}
}
