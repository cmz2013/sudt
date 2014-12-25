package cn.tool.ui.dialog;

import java.util.List;

import javax.swing.JDialog;

import cn.tool.config.ToolConfig;
import cn.tool.deploy.scheme.CommandLine;
/**
 * 导出命令对话框
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class CmdExportDialog extends FileOutputDialog {
	
	public CmdExportDialog(List<CommandLine> cmds) {
		super(cmds);
		setTitle(ToolConfig.i18.getProperty("soft.deploy.tool.command.list.export"));
		setVisible(true);
	}

	@Override
	protected JDialog getDialog() {
		return this;
	}
	
}
