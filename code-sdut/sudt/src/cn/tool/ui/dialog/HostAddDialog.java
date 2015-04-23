package cn.tool.ui.dialog;

import javax.swing.JOptionPane;

import cn.tool.config.ToolConfig;
import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.panel.HostTablePanel;
import cn.tool.ui.resour.SystemConst;
/**
* 添加主机对话框
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public class HostAddDialog extends AbstractHostDialog {
	
	private int updateRow;
	private HostTablePanel hostPanel;

	public HostAddDialog(HostTablePanel hostPanel) {
		super(SystemConst.DIALOG_ADD, 
			"soft.deploy.tool.host.list.add", hostPanel.getTable());
		this.hostPanel = hostPanel;
		userField.setText(ToolConfig.user.getDefaultUser());
	}

	@Override
	public void clearContext() {
		ipField.setText("");
		portField.setText("");
		userField.setText(ToolConfig.user.getDefaultUser());
		pasField.setText("");
		ipField.requestFocus();
	}
	
	@Override
	protected String sureExecute() throws Exception {
		if (SystemConst.DIALOG_UPDATE == getDialogType()) {
			TableUtils.updateRow(hostList, updateRow, 
					ipField.getText(), 
					portField.getText(),
					userField.getText(), 
					pasField.getText());
		} else {
			TableUtils.appendRow(hostList, 
					ipField.getText(), 
					portField.getText(),
					userField.getText(), 
					pasField.getText());
		}
		hostPanel.getHostExportBtn().setEnabled(true);
		return null;
	}

	public AbstractDialog getDialog() {
		return this;
	}

	@Override
	protected boolean validates() {
		setDialogType(SystemConst.DIALOG_ADD);
		String ip = ipField.getText();
		for (int i = 0; i < hostList.getRowCount(); i++) {
			if (hostList.getValueAt(i, 0).equals(ip)) {
				updateRow = i;
				int option = JOptionPane.showConfirmDialog(null, 
						String.format(ToolConfig.i18.getProperty(
							"soft.deploy.tool.host.list.add.exist"), ip), 
						ToolConfig.i18.getProperty("soft.deploy.tool.confirm"), 
						JOptionPane.WARNING_MESSAGE);
				if (JOptionPane.OK_OPTION == option) {
					setDialogType(SystemConst.DIALOG_UPDATE);
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
}
