package cn.sudt.ui.dialog;

import java.util.List;

import cn.sudt.config.ToolConfig;
import cn.sudt.deploy.scheme.CommandLine;
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
		setTitle(ToolConfig.i18.getProperty("sudt.command.list.export"));
		setVisible(true);
	}

	@Override
	public AbstractDialog getDialog() {
		return this;
	}
	
}
