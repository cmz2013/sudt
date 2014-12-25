package cn.tool.ui.dialog;

import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;

import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.panel.CmdTablePanel;
import cn.tool.ui.panel.TablePanel;
import cn.tool.ui.resour.SystemConst;
/**
* 更新命令对话
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public class CmdUpdateDialog extends AbstractCmdDialog {

	private TablePanel tablePanel;
	
	public CmdUpdateDialog(CmdTablePanel tablePanel) {
		super(SystemConst.DIALOG_UPDATE, 
			"soft.deploy.tool.command.list.update");
		this.tablePanel = tablePanel;
		initDialogData();
	}

	public void initDialogData() {
		int selRow = tablePanel.getTable().getSelectedRow();
		DefaultTableModel tableModel = (DefaultTableModel) 
				tablePanel.getTable().getModel();
		cmdExp.setText((String) tableModel.getValueAt(selRow, 0));
		retry.setSelected((boolean) tableModel.getValueAt(selRow, 1));
		delay.setValue(tableModel.getValueAt(selRow, 2));
	}

	@Override
	public void clearContext() {
		cmdExp.requestFocus();
	}
	
	@Override
	protected JDialog getDialog() {
		return this;
	}

	@Override
	protected String sureExecute() throws Exception {
		TableUtils.updateRow(tablePanel.getTable(), 
				tablePanel.getTable().getSelectedRow(), 
				cmdExp.getText(), 
				retry.isSelected(),
				delay.getValue());
		return null;
	}
}