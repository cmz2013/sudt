package cn.sudt.ui.dialog;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cn.sudt.config.ToolConfig;
import cn.sudt.lang.swing.TableUtils;
import cn.sudt.ui.common.MessageType;
import cn.sudt.ui.common.UIConst;
/**
* 更新主机对话框
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public class HostUpdateDialog extends AbstractHostDialog {
	
	public HostUpdateDialog(JTable hostList) {
		super(UIConst.DIALOG_UPDATE, "sudt.host.list.update", hostList);
		initDialogData();
	}

	public void initDialogData() {
		int selRow = hostList.getSelectedRow();
		DefaultTableModel tableModel = (DefaultTableModel) hostList.getModel();
		this.ipField.setText(tableModel.getValueAt(selRow, 0) + "");
		this.portField.setText(tableModel.getValueAt(selRow, 1) + "");
		this.userField.setText(tableModel.getValueAt(selRow, 2) + "");
		this.pasField.setText(tableModel.getValueAt(selRow, 3) + "");
	}

	@Override
	public void clearContext() {
		ipField.requestFocus();
	}
	
	@Override
	protected String sureExecute() throws Exception {
		TableUtils tableUtils = new TableUtils();
		tableUtils.updateRow(hostList, 
				hostList.getSelectedRow(), 
				ipField.getText(), 
				portField.getText(),
				userField.getText(), 
				pasField.getText());
		
		return null;
	}

	@Override
	public AbstractDialog getDialog() {
		return this;
	}

	@Override
	protected boolean validates() {
		String ip = ipField.getText();
		String port = portField.getText();
		
		for (int i = 0; i < hostList.getRowCount(); i++) {
			if (hostList.getSelectedRow() != i && 
					hostList.getValueAt(i, 0).equals(ip) &&
					hostList.getValueAt(i, 1).equals(port) ) {
				
				String infoFormat = ToolConfig.i18.getProperty(
						"sudt.host.list.update.exist");
				showInfo(MessageType.error, 
						String.format(infoFormat, ip, port));
				return false;
			}
		}
		return true;
	}
	
}
