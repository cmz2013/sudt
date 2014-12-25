package cn.tool.ui.tab;

import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import cn.tool.config.ToolConfig;
import cn.tool.deploy.DeployManager;
import cn.tool.deploy.Controller;
import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.frame.ConsoleFrame;
import cn.tool.ui.panel.BottomlPanel;
import cn.tool.ui.panel.CmdTablePanel;
import cn.tool.ui.panel.HostTablePanel;
import cn.tool.ui.panel.SplitPane;
import cn.tool.ui.resour.IconContainer;
import cn.tool.ui.resour.SystemConst;
/**
 * 主窗口-升级部署Tab页
 * 
 * @author chongming
 *
 */
public class DeployTabbed {
	
	/**
	 * 部署面板
	 */
	private SplitPane deployPanel;
	
	private BottomlPanel bottomPanel = new BottomlPanel();
	private HostTablePanel hostTablePanel = new HostTablePanel(TableUtils.getDefaultTableModel(
			ToolConfig.i18.getProperty("soft.deploy.tool.host.list"), "", ""));
	private ConsoleFrame console = null;
	
	@SuppressWarnings("serial")
	private CmdTablePanel cmdTablePanel = new CmdTablePanel(){

		@Override
		public void cmdStopBtnAction() {
			DeployManager.setPause(false);
			DeployManager.setStop(true);
			this.getCmdExeBtn().setEnabled(false);
			Controller.interrupt();
		}

		@Override
		public void cmdExeBtnAction() {
			try {
				if (ToolConfig.i18.getProperty("soft.deploy.tool.button.execute"
						).equals(cmdTablePanel.getCmdExeBtn().getToolTipText())) {
					
					if (!cmdTablePanel.getCmdStopBtn().isEnabled()) {
						
						if (0 == getHostTablePanel().getTable().getRowCount()) {
							JOptionPane.showMessageDialog(null, 
									ToolConfig.i18.getProperty("soft.deploy.tool.deploy.host.null"), 
									ToolConfig.i18.getProperty("soft.deploy.tool.tip"), 
									JOptionPane.OK_OPTION);
							return;
						}
						
						if (0 == getCmdTablePanel().getTable().getRowCount()) {
								JOptionPane.showMessageDialog(null, 
										ToolConfig.i18.getProperty("soft.deploy.tool.deploy.command.null"), 
										ToolConfig.i18.getProperty("soft.deploy.tool.tip"), 
										JOptionPane.OK_OPTION);
								return;
							}
						
						closeDialog(hostTablePanel.getHostUpdateDialog(),
								hostTablePanel.getHostAddDialog(), 
								hostTablePanel.getHostExportDialog(), 
								hostTablePanel.getHostImportDialog(), 
								cmdTablePanel.getCmdExportDialog(), 
								cmdTablePanel.getCmdImportDialog(), 
								cmdTablePanel.getCmdAddDialog(), 
								cmdTablePanel.getCmdInsetDialog(), 
								cmdTablePanel.getCmdUpdateDialog());
						
						console = ConsoleFrame.showConsole();
						new DeployManager().start();
					} else {
						DeployManager.setPause(false);
					}
					setBtnStatus(false);
				} else if (ToolConfig.i18.getProperty("soft.deploy.tool.button.pause"
						).equals(cmdTablePanel.getCmdExeBtn().getToolTipText())) {
					setButton(cmdTablePanel.getCmdExeBtn(), ToolConfig.i18.getProperty(
							"soft.deploy.tool.button.execute"), IconContainer.icon_execute);
					DeployManager.setPause(true);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, 
						e.getMessage(),
						ToolConfig.i18.getProperty("soft.deploy.tool.error"),
						JOptionPane.WARNING_MESSAGE);
			}
		}
		
	};
	
	public DeployTabbed() {
		init();
		setLayout();
	}

	private void init() {
		deployPanel = new SplitPane(hostTablePanel, cmdTablePanel);
		DeployManager.setDeployTab(this);
	}

	private void setButton(JButton btn, String tip, ImageIcon icon) {
		btn.setToolTipText(tip);
		btn.setIcon(icon);
	}

	private void setLayout() {
		TableUtils.hideColumn(hostTablePanel.getTable(), 1, 2);
		TableUtils.hideColumn(cmdTablePanel.getTable(), 1, 2);
		hostTablePanel.getTable().getColumnModel().getColumn(0).setMinWidth(SystemConst.TABLE_WIDTH);
		cmdTablePanel.getTable().getColumnModel().getColumn(0).setMinWidth(SystemConst.TABLE_WIDTH);
		
		cmdTablePanel.add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * 关闭对话框
	 */
	private void closeDialog(Dialog... dialogs) {
		for (Dialog dialog : dialogs) {
			if (null != dialog && dialog.isVisible()) {
				dialog.setVisible(false);
			}
		}
	}

	/**
	 * 项目部署时，按钮状态置为：false
	 * 
	 * @param status
	 */
	public void setBtnStatus(boolean status) {
		cmdTablePanel.getCmdStopBtn().setEnabled(!status);
		hostTablePanel.getAddBtn().setEnabled(status);
		hostTablePanel.getDelBtn().setEnabled(status);
		hostTablePanel.getHostImportBtn().setEnabled(status);
		hostTablePanel.getHostExportBtn().setEnabled(status);
		cmdTablePanel.getAddBtn().setEnabled(status);
		cmdTablePanel.getDelBtn().setEnabled(status);
		cmdTablePanel.getCmdUpBtn().setEnabled(status);
		cmdTablePanel.getCmdDownBtn().setEnabled(status);
		cmdTablePanel.getCmdImportBtn().setEnabled(status);
		cmdTablePanel.getCmdExportBtn().setEnabled(status);
		bottomPanel.getBusyIconLabel().setEnabled(!status);
		
		if (status) {
			setButton(cmdTablePanel.getCmdExeBtn(), ToolConfig.i18.getProperty(
					"soft.deploy.tool.button.execute"), IconContainer.icon_execute);
			if (cmdTablePanel.getTable().getSelectedRow() >= 0) {
				cmdTablePanel.getCmdInsertBtn().setEnabled(status);
			}
		} else {
			cmdTablePanel.getCmdInsertBtn().setEnabled(status);
			setButton(cmdTablePanel.getCmdExeBtn(), ToolConfig.i18.getProperty(
					"soft.deploy.tool.button.pause"), IconContainer.icon_pause);
		}
		hostTablePanel.setTableEnabled(status);
		cmdTablePanel.setTableEnabled(status);
		DeployManager.setStop(status);
	}

	public SplitPane getDeployPanel() {
		return deployPanel;
	}

	public ConsoleFrame getConsole() {
		return console;
	}

	public HostTablePanel getHostTablePanel() {
		return hostTablePanel;
	}

	public CmdTablePanel getCmdTablePanel() {
		return cmdTablePanel;
	}

	public BottomlPanel getBottomPanel() {
		return bottomPanel;
	}
	
}