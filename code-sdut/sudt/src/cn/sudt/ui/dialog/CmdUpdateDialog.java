package cn.sudt.ui.dialog;

import javax.swing.table.DefaultTableModel;

import cn.sudt.ui.common.UIConst;
import cn.sudt.ui.panel.CmdTablePanel;
/**
* 更新命令对话
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public class CmdUpdateDialog extends AbstractCmdDialog {

	private CmdTablePanel cmdTablePanel;
	
	public CmdUpdateDialog(CmdTablePanel cmdTablePanel) {
		super(UIConst.DIALOG_UPDATE, "sudt.command.list.update");
		this.cmdTablePanel = cmdTablePanel;
		initDialogData();
	}

	public void initDialogData() {
		int selRow = cmdTablePanel.getTable().getSelectedRow();
		DefaultTableModel tableModel = (DefaultTableModel) 
				cmdTablePanel.getTable().getModel();
		cmdExp.setText((String) tableModel.getValueAt(selRow, 0));
		retry.setSelected((boolean) tableModel.getValueAt(selRow, 1));
		delay.setValue(tableModel.getValueAt(selRow, 2));
	}

	@Override
	public void clearContext() {
		cmdExp.requestFocus();
	}
	
	@Override
	public AbstractDialog getDialog() {
		return this;
	}

	@Override
	protected String sureExecute() throws Exception {
		cmdTablePanel.getTableUtils().updateRow(
				cmdTablePanel.getTable(), 
				cmdTablePanel.getTable().getSelectedRow(), 
				cmdExp.getText(), 
				retry.isSelected(),
				delay.getValue());
		
		return null;
	}
}
