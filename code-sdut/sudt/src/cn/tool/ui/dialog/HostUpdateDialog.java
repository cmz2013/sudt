package cn.tool.ui.dialog;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cn.tool.config.ToolConfig;
import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.resour.IconContainer;
import cn.tool.ui.resour.SystemConst;
/**
* 更新主机对话框
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public class HostUpdateDialog extends AbstractHostDialog {
	public HostUpdateDialog(JTable hostList) {
		super(SystemConst.DIALOG_UPDATE, "soft.deploy.tool.host.list.update", hostList);
		initDialogData();
	}

	public void initDialogData() {
		int selRow = hostList.getSelectedRow();
		DefaultTableModel tableModel = (DefaultTableModel) hostList.getModel();
		this.ipField.setText((String) tableModel.getValueAt(selRow, 0));
		this.userField.setText((String) tableModel.getValueAt(selRow, 1));
		this.pasField.setText((String) tableModel.getValueAt(selRow, 2));
	}

	@Override
	public void clearContext() {
		ipField.requestFocus();
	}
	
	@Override
	protected String sureExecute() throws Exception {
		TableUtils.updateRow(hostList, 
				hostList.getSelectedRow(), 
				ipField.getText(), 
				userField.getText(), 
				pasField.getText());
		return null;
	}

	@Override
	protected JDialog getDialog() {
		return this;
	}

	@Override
	protected boolean validates() {
		String ip = ipField.getText();
		for (int i = 0; i < hostList.getRowCount(); i++) {
			if (hostList.getSelectedRow() != i && hostList.getValueAt(i, 0).equals(ip)) {
				showInfo(IconContainer.icon_error, Color.RED, String.format(
						ToolConfig.i18.getProperty("soft.deploy.tool.host.list.update.exist"), ip));
				return false;
			}
		}
		return true;
	}
	
}