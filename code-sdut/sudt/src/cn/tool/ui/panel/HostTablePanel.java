package cn.tool.ui.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import cn.tool.deploy.scheme.HostInfo;
import cn.tool.lang.swing.ComFactory;
import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.dialog.HostAddDialog;
import cn.tool.ui.dialog.HostExportDialog;
import cn.tool.ui.dialog.HostImportDialog;
import cn.tool.ui.dialog.HostUpdateDialog;
import cn.tool.ui.resour.IconContainer;
/**
 * 主机列表面板
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class HostTablePanel extends TablePanel {

	private HostUpdateDialog hostUpdateDialog = null;
	private HostAddDialog hostAddDialog = null;
	private HostExportDialog hostExportDialog = null;
	private HostImportDialog hostImportDialog = null;
	
	private JButton hostExportBtn = ComFactory.getButton(
			"soft.deploy.tool.button.export", IconContainer.icon_export);
	private JButton hostImportBtn = ComFactory.getButton(
			"soft.deploy.tool.button.import", IconContainer.icon_import);
	
	public HostTablePanel(DefaultTableModel model) {
		super(model);
		setLayout();
		setListener();
	}
	
	/**
	 * 不会重写父类的setListener()方法，因为父类私有
	 */
	private void setListener() {
		setHostImportBtnActionListener(this);
		setHostExportBtnActionListener();
	}
	
	/**
	 * 不会重写父类的setLayout()方法，因为父类私有
	 */
	private void setLayout() {
		hostExportBtn.setEnabled(false);
		Component[] hostBtns = { hostImportBtn, hostExportBtn };
		for (int i = 0; i < hostBtns.length; i++) {
			getBtnPanel().add(hostBtns[i]);
		}
	}

	@Override
	protected boolean setDelBtnAction() {
		boolean delStatus = TableUtils.deleteRow(getTable(),
				"soft.deploy.tool.host.list.delete");
		if (delStatus) {
			if (table.getRowCount() <= 0) {
				hostExportBtn.setEnabled(false);
			}
		}
		return delStatus;
	}
	
	@Override
	protected boolean setAddBtnAction() {
		if (null == hostAddDialog) {
			hostAddDialog = new HostAddDialog(this);
		} else {
			hostAddDialog.setVisible(true);
		}
		return true;
	}

	@Override
	protected void setTableMouseListener(MouseEvent mouseevent) {
		if (!tableEnabled) {
			return;
		}
		
		if (mouseevent.getClickCount() >= 2) {
			if (null == hostUpdateDialog) {
				hostUpdateDialog = new HostUpdateDialog(getTable());
			} else {
				hostUpdateDialog.initDialogData();
				hostUpdateDialog.setVisible(true);
			}
		}
	}
	
	private void setHostExportBtnActionListener() {
		hostExportBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (0 == getTable().getRowCount()) {
					return;
				}
				
				if (null == hostExportDialog) {
					hostExportDialog = new HostExportDialog(getHosts());
				} else {
					hostExportDialog.setVisible(true);
				}
			}
		});
	}
	
	/**
	 * 获取主机列表数据
	 * 
	 * @return
	 */
	public List<HostInfo> getHosts() {
		List<HostInfo> hosts = new ArrayList<HostInfo>();
		for (int row = 0; row < getTable().getRowCount(); row++) {
			hosts.add(new HostInfo(
				(String)getTable().getValueAt(row, 0), 
				(String)getTable().getValueAt(row, 1), 
				(String)getTable().getValueAt(row, 2)));
		}
		return hosts;
	}
	
	
	private void setHostImportBtnActionListener(
			final HostTablePanel tablePanel) {
		hostImportBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (null == hostImportDialog) {
					hostImportDialog = new HostImportDialog(tablePanel);
				} else {
					hostImportDialog.setVisible(true);
				}
			}
		});
		
	}

	public HostUpdateDialog getHostUpdateDialog() {
		return hostUpdateDialog;
	}

	public HostAddDialog getHostAddDialog() {
		return hostAddDialog;
	}

	public JButton getHostExportBtn() {
		return hostExportBtn;
	}

	public JButton getHostImportBtn() {
		return hostImportBtn;
	}

	public HostExportDialog getHostExportDialog() {
		return hostExportDialog;
	}

	public HostImportDialog getHostImportDialog() {
		return hostImportDialog;
	}

}
