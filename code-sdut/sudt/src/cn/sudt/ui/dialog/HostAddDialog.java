package cn.sudt.ui.dialog;

import javax.swing.JOptionPane;

import cn.sudt.config.ToolConfig;
import cn.sudt.ui.common.UIConst;
import cn.sudt.ui.panel.HostTablePanel;
/**
* 添加主机对话框
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public class HostAddDialog extends AbstractHostDialog {
	
	private int updateRow;
	
	private HostTablePanel hostTablePanel;

	public HostAddDialog(HostTablePanel hostTablePanel) {
		super(UIConst.DIALOG_ADD, 
			"sudt.host.list.add", hostTablePanel.getTable());
		this.hostTablePanel = hostTablePanel;
		userField.setText(ToolConfig.user.getDefaultUser());
		portField.setText(ToolConfig.user.getDefaultPort());
	}

	@Override
	public void clearContext() {
		ipField.setText("");
		portField.setText("");
		userField.setText(ToolConfig.user.getDefaultUser());
		portField.setText(ToolConfig.user.getDefaultPort());
		pasField.setText("");
		ipField.requestFocus();
	}
	
	@Override
	protected String sureExecute() throws Exception {
		if (UIConst.DIALOG_UPDATE == getDialogType()) {
			hostTablePanel.getTableUtils().updateRow(
					hostList, updateRow, 
					ipField.getText(), 
					portField.getText(),
					userField.getText(), 
					pasField.getText());
		} else {
			hostTablePanel.getTableUtils().appendRow(
					hostList, 
					ipField.getText(), 
					portField.getText(),
					userField.getText(), 
					pasField.getText());
		}
		
		hostTablePanel.getHostExportBtn().setEnabled(true);
		return null;
	}

	public AbstractDialog getDialog() {
		return this;
	}

	@Override
	protected boolean validates() {
		setDialogType(UIConst.DIALOG_ADD);
		String ip = ipField.getText().trim();
		String port = portField.getText().trim();
		
		for (int i = 0; i < hostList.getRowCount(); i++) {
			if (hostList.getValueAt(i, 0).equals(ip) &&
					hostList.getValueAt(i, 1).equals(port)) {
				
				updateRow = i;
				int option = JOptionPane.showConfirmDialog(null, 
						String.format(ToolConfig.i18.getProperty(
							"sudt.host.list.add.exist"), ip, port), 
						ToolConfig.i18.getProperty("sudt.title.confirm"), 
						JOptionPane.WARNING_MESSAGE);
				
				if (JOptionPane.OK_OPTION == option) {
					setDialogType(UIConst.DIALOG_UPDATE);
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
}
