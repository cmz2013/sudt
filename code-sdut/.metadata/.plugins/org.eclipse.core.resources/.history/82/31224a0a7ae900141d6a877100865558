package cn.tool.ui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import cn.tool.config.ToolConfig;
import cn.tool.deploy.scheme.CommandLine;
import cn.tool.lang.swing.SwingFactory;
import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.dialog.CmdAddDialog;
import cn.tool.ui.dialog.CmdExportDialog;
import cn.tool.ui.dialog.CmdImportDialog;
import cn.tool.ui.dialog.CmdInsertDialog;
import cn.tool.ui.dialog.CmdUpdateDialog;
import cn.tool.ui.resour.IconContainer;
/**
 * 命令列表面板
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public abstract class CmdTablePanel extends TablePanel {
	
	private CmdInsertDialog cmdInsetDialog = null;
	private CmdAddDialog cmdAddDialog = null;
	private CmdUpdateDialog cmdUpdateDialog = null;
	private CmdExportDialog cmdExportDialog = null;
	private CmdImportDialog cmdImportDialog = null;
	
	private JButton cmdInsertBtn = SwingFactory.getButton(
			"soft.deploy.tool.button.insert", IconContainer.icon_insert);
	private JButton cmdImportBtn = SwingFactory.getButton(
			"soft.deploy.tool.button.import", IconContainer.icon_import);
	private JButton cmdExportBtn = SwingFactory.getButton(
			"soft.deploy.tool.button.export", IconContainer.icon_export);
	private JButton cmdUpBtn = SwingFactory.getButton(
			"soft.deploy.tool.button.up", IconContainer.icon_up);
	private JButton cmdDownBtn = SwingFactory.getButton(
			"soft.deploy.tool.button.down", IconContainer.icon_down);
	private JButton cmdExeBtn = SwingFactory.getButton(
			"soft.deploy.tool.button.execute", IconContainer.icon_execute);
	private JButton cmdStopBtn = SwingFactory.getButton(
			"soft.deploy.tool.button.stop", IconContainer.icon_stop);
	
	public CmdTablePanel() {
		super(TableUtils.getDefaultTableModel(
				ToolConfig.i18.getProperty("soft.deploy.tool.command.list"), "", ""));
		init();
		setLayout();
		setListener(this);
	}

	private void init() {
		cmdInsertBtn.setEnabled(false);
		cmdUpBtn.setEnabled(false);
		cmdDownBtn.setEnabled(false);
		cmdExportBtn.setEnabled(false);
		cmdStopBtn.setEnabled(false);
	}
	
	private void setLayout() {
		JButton[] cmdBtns = {cmdInsertBtn, cmdUpBtn, cmdDownBtn, 
			cmdImportBtn, cmdExportBtn, cmdExeBtn, cmdStopBtn};
		for (int i = 0; i < cmdBtns.length; i++) {
			getBtnPanel().add(cmdBtns[i]);
		}
	}
	
	private void setListener(CmdTablePanel tablePanel) {
		setCmdDownBtnActionListener();
		setCmdUpBtnActionListener();
		setCmdImportBtnActionListener(tablePanel);
		setCmdExportBtnActionListener();
		setCmdInsertBtnActionListener(tablePanel);
		setCmdStopBtnActionListener();
		setCmdExeBtnActionListener();
	}
	
	private void setCmdExeBtnActionListener() {
		cmdExeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				cmdExeBtnAction();
			}
		});
	}
	
	public abstract void cmdExeBtnAction();
	
	private void setCmdUpBtnActionListener() {
		cmdUpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				TableUtils.moveRows(getTable(), true);
				if (getTable().getSelectedRow() <= 0) {
					cmdUpBtn.setEnabled(false);
				} 
				cmdDownBtn.setEnabled(true);
			}
		});
		
	}
	
	private void setCmdExportBtnActionListener() {
		cmdExportBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (0 == getTable().getRowCount()) {
					return;
				}
				
				if (null == cmdExportDialog) {
					cmdExportDialog = new CmdExportDialog(getCommands());
				} else {
					cmdExportDialog.setVisible(true);
				}
			}
		});
	}
	
	/**
	 * 获取命令列表数据
	 * 
	 * @return
	 */
	public List<CommandLine> getCommands() {
		List<CommandLine> cmds = new ArrayList<CommandLine>();
		for (int row = 0; row < getTable().getRowCount(); row++) {
			cmds.add(new CommandLine(
					(String)getTable().getValueAt(row, 0), 
					(boolean)getTable().getValueAt(row, 1), 
					(int)getTable().getValueAt(row, 2) * 1000));
		}
		return cmds;
	}

	private void setCmdStopBtnActionListener() {
		cmdStopBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				cmdStopBtnAction();
			}
		});
	}
	
	public abstract void cmdStopBtnAction();
	
	private void setCmdImportBtnActionListener(final CmdTablePanel tablePanel) {
		cmdImportBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (null == cmdImportDialog ) {
					cmdImportDialog = new CmdImportDialog(tablePanel);
				} else {
					cmdImportDialog.setVisible(true);
				}
			}
		});
	}

	private void setCmdInsertBtnActionListener(
			final CmdTablePanel tablePanel) {
		cmdInsertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				if (null == cmdInsetDialog ) {
					cmdInsetDialog = new CmdInsertDialog(tablePanel);
				} else {
					cmdInsetDialog.setVisible(true);
				}
			}
		});
		
	}
	
	private void setCmdDownBtnActionListener() {
		cmdDownBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				TableUtils.moveRows(getTable(), false);
				
				if ((getTable().getSelectedRow() + getTable().getSelectedRows(
						).length) >= getTable().getRowCount()) {
					cmdDownBtn.setEnabled(false);
				}
				cmdUpBtn.setEnabled(true);
			}
		});
		
	}
	
	@Override
	protected void setTableMouseListener(MouseEvent mouseevent) {
		if (!tableEnabled) {
			return;
		}
		
		if (getTable().getSelectedRow() >= 0) {
			cmdInsertBtn.setEnabled(true);
		}
		
		if (getTable().getSelectedRow() > 0) {
			cmdUpBtn.setEnabled(true);
		} else {
			cmdUpBtn.setEnabled(false);
		}
		
		if ((getTable().getSelectedRow() + getTable().getSelectedRows(
				).length) < getTable().getRowCount()) {
			cmdDownBtn.setEnabled(true);
		} else {
			cmdDownBtn.setEnabled(false);
		}
		
		if (mouseevent.getClickCount() >= 2) {
			if (null == cmdUpdateDialog) {
				cmdUpdateDialog = new CmdUpdateDialog(this);
			} else {
				cmdUpdateDialog.initDialogData();
				cmdUpdateDialog.setVisible(true);
			}
		}
	}
	
	@Override
	protected boolean setDelBtnAction() {
		boolean delStatus = TableUtils.deleteRow(getTable(), 
				"soft.deploy.tool.command.list.delete");
		cmdInsertBtn.setEnabled(!delStatus);
		if (delStatus) {
			cmdUpBtn.setEnabled(false);
			cmdDownBtn.setEnabled(false);
			if (table.getRowCount() <= 0) {
				cmdExportBtn.setEnabled(false);
			}
		}
		return delStatus;
	}
	
	@Override
	protected boolean setAddBtnAction() {
		if (null == cmdAddDialog ) {
			cmdAddDialog = new CmdAddDialog(this);
		} else {
			cmdAddDialog.setVisible(true);
		}
		return true;
	}

	public CmdInsertDialog getCmdInsetDialog() {
		return cmdInsetDialog;
	}

	public CmdAddDialog getCmdAddDialog() {
		return cmdAddDialog;
	}

	public CmdUpdateDialog getCmdUpdateDialog() {
		return cmdUpdateDialog;
	}

	public JButton getCmdInsertBtn() {
		return cmdInsertBtn;
	}

	public JButton getCmdImportBtn() {
		return cmdImportBtn;
	}

	public JButton getCmdExportBtn() {
		return cmdExportBtn;
	}

	public JButton getCmdUpBtn() {
		return cmdUpBtn;
	}

	public JButton getCmdDownBtn() {
		return cmdDownBtn;
	}

	public JButton getCmdExeBtn() {
		return cmdExeBtn;
	}

	public JButton getCmdStopBtn() {
		return cmdStopBtn;
	}

	public CmdExportDialog getCmdExportDialog() {
		return cmdExportDialog;
	}

	public CmdImportDialog getCmdImportDialog() {
		return cmdImportDialog;
	}
	
}
