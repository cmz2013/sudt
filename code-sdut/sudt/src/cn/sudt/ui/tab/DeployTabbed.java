package cn.sudt.ui.tab;

import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import cn.sudt.config.ToolConfig;
import cn.sudt.deploy.DeployManager;
import cn.sudt.ui.common.IconContainer;
import cn.sudt.ui.common.UIConst;
import cn.sudt.ui.frame.ConsoleFrame;
import cn.sudt.ui.panel.BottomlPanel;
import cn.sudt.ui.panel.CmdTablePanel;
import cn.sudt.ui.panel.HostTablePanel;
import cn.sudt.ui.panel.SplitPane;
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
	private HostTablePanel hostTablePanel;
	
	private ConsoleFrame consoleFrame = null;
	private DeployManager deployManager = null;
	private CmdTablePanel cmdTablePanel;
	
	public DeployTabbed() {
		init();
		setLayout();
	}
	
	private void initDeployManager() {
		if (null != this.deployManager) {
			if (this.deployManager.getConcurrentController().isEnd()) {
				this.deployManager = new DeployManager(this);
			}
		} else {
			this.deployManager = new DeployManager(this);
		}
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
	
	@SuppressWarnings("serial")
	private void init() {
		this.consoleFrame = new ConsoleFrame();
		this.hostTablePanel = new HostTablePanel(
				ToolConfig.i18.getProperty("sudt.host.list"), 
				ToolConfig.i18.getProperty("sudt.host.port"), 
				"", "");
		
		this.cmdTablePanel = new CmdTablePanel(){
			@Override
			public void cmdExeBtnAction() {
				try {
					initDeployManager();
					
					if (ToolConfig.i18.getProperty("sudt.button.execute"
							).equals(cmdTablePanel.getCmdExeBtn().getToolTipText())) {
						
						if (!cmdTablePanel.getCmdStopBtn().isEnabled()) {
							
							if (0 == getHostTablePanel().getTable().getRowCount()) {
								JOptionPane.showMessageDialog(null, 
										ToolConfig.i18.getProperty("sudt.deploy.host.null"), 
										ToolConfig.i18.getProperty("sudt.title.tip"), 
										JOptionPane.OK_OPTION);
								return;
							}
							
							if (0 == getCmdTablePanel().getTable().getRowCount()) {
									JOptionPane.showMessageDialog(null, 
											ToolConfig.i18.getProperty("sudt.deploy.command.null"), 
											ToolConfig.i18.getProperty("sudt.title.tip"), 
											JOptionPane.OK_OPTION);
									return;
							}
							
							int option = JOptionPane.showConfirmDialog(null, 
									ToolConfig.i18.getProperty("sudt.deploy.sure"),
									ToolConfig.i18.getProperty("sudt.title.tip"),
									JOptionPane.OK_CANCEL_OPTION);
							
							if (JOptionPane.OK_OPTION != option) {
								return;
							}
							setBtnStatus(false);
							
							closeDialog(hostTablePanel.getHostUpdateDialog(),
									hostTablePanel.getHostAddDialog(), 
									hostTablePanel.getHostImportDialog(), 
									cmdTablePanel.getCmdImportDialog(), 
									cmdTablePanel.getCmdAddDialog(), 
									cmdTablePanel.getCmdInsetDialog(), 
									cmdTablePanel.getCmdUpdateDialog());
							
							consoleFrame.showConsoleFrame();
							deployManager.start();
						} else {
							deployManager.setPause(false);
							setButton(cmdTablePanel.getCmdExeBtn(), ToolConfig.i18.getProperty(
									"sudt.button.pause"), IconContainer.icon_pause);
						}
					} else if (ToolConfig.i18.getProperty("sudt.button.pause"
							).equals(cmdTablePanel.getCmdExeBtn().getToolTipText())) {
						setButton(cmdTablePanel.getCmdExeBtn(), ToolConfig.i18.getProperty(
								"sudt.button.execute"), IconContainer.icon_execute);
						deployManager.setPause(true);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, 
							e.getMessage(),
							ToolConfig.i18.getProperty("sudt.title.error"),
							JOptionPane.WARNING_MESSAGE);
				}
			}

			@Override
			public void cmdStopBtnAction() {
				this.getCmdStopBtn().setEnabled(false);
				this.getCmdExeBtn().setEnabled(false);
				
				deployManager.setPause(false);
				deployManager.setInterrupted(true);
				deployManager.getConcurrentController().interrupt();
			}
			
		};
		
		this.deployPanel = new SplitPane(hostTablePanel, cmdTablePanel);
	}

	private void setButton(JButton btn, String tip, ImageIcon icon) {
		btn.setToolTipText(tip);
		btn.setIcon(icon);
	}

	private void setLayout() {
		deployPanel.setDividerSize(1);
		hostTablePanel.getTableUtils().hideColumn(hostTablePanel.getTable(), 2, 3);
		hostTablePanel.getTableUtils().hideColumn(cmdTablePanel.getTable(), 1, 2);
		hostTablePanel.getTable().getColumnModel(
				).getColumn(0).setMinWidth(UIConst.TABLE_WIDTH/2);
		hostTablePanel.getTable().getColumnModel(
				).getColumn(1).setMinWidth(UIConst.TABLE_WIDTH/2);
		cmdTablePanel.getTable().getColumnModel(
				).getColumn(0).setMinWidth(UIConst.TABLE_WIDTH);
		
		cmdTablePanel.add(bottomPanel, BorderLayout.SOUTH);
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
		cmdTablePanel.getAddBtn().setEnabled(status);
		cmdTablePanel.getDelBtn().setEnabled(status);
		cmdTablePanel.getCmdImportBtn().setEnabled(status);
		bottomPanel.getBusyIconLabel().setEnabled(!status);
		
		if (status) {
			setButton(cmdTablePanel.getCmdExeBtn(), ToolConfig.i18.getProperty(
					"sudt.button.execute"), IconContainer.icon_execute);
			if (cmdTablePanel.getTable().getSelectedRow() >= 0) {
				cmdTablePanel.getCmdInsertBtn().setEnabled(status);
			}
		} else {
			cmdTablePanel.getCmdUpBtn().setEnabled(status);
			cmdTablePanel.getCmdDownBtn().setEnabled(status);
			cmdTablePanel.getTable().clearSelection();
			cmdTablePanel.getCmdInsertBtn().setEnabled(status);
			hostTablePanel.getTable().clearSelection();
			setButton(cmdTablePanel.getCmdExeBtn(), ToolConfig.i18.getProperty(
					"sudt.button.pause"), IconContainer.icon_pause);
		}
		hostTablePanel.setTableEnabled(status);
		cmdTablePanel.setTableEnabled(status);
	}

	public SplitPane getDeployPanel() {
		return deployPanel;
	}

	public ConsoleFrame getConsoleFrame() {
		return consoleFrame;
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
