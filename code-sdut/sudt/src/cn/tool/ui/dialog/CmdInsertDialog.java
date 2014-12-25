package cn.tool.ui.dialog;

import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.panel.CmdTablePanel;
/**
 * 插入命令对话框
 *
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class CmdInsertDialog extends CmdAddDialog {
	
	private CmdTablePanel cmdPanel;
	
	public CmdInsertDialog(CmdTablePanel cmdPanel) {
		super(cmdPanel);
		setModal(true);
		this.cmdPanel = cmdPanel;
	}

	@Override
	protected String sureExecute() throws Exception {
		TableUtils.insertRow(cmdPanel.getTable(), 
				cmdPanel.getTable().getSelectedRow(),
				cmdExp.getText(), 
				retry.isSelected(),
				delay.getValue());
		
		cmdPanel.getCmdExportBtn().setEnabled(true);
		
		return null;
	}
}

