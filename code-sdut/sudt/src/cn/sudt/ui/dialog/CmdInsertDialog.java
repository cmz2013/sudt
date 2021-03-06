package cn.sudt.ui.dialog;

import cn.sudt.ui.panel.CmdTablePanel;
/**
 * 插入命令对话框
 *
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class CmdInsertDialog extends CmdAddDialog {
	
	private CmdTablePanel cmdTablePanel;
	
	public CmdInsertDialog(CmdTablePanel cmdTablePanel) {
		super(cmdTablePanel);
		setModal(true);
		this.cmdTablePanel = cmdTablePanel;
	}

	@Override
	protected String sureExecute() throws Exception {
		cmdTablePanel.getTableUtils().insertRow(
				cmdTablePanel.getTable(), 
				cmdTablePanel.getTable().getSelectedRow(),
				cmdExp.getText(), 
				retry.isSelected(),
				delay.getValue());
		
		cmdTablePanel.getCmdExportBtn().setEnabled(true);
		
		return null;
	}
}

