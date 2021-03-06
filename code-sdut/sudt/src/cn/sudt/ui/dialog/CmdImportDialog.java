package cn.sudt.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.sudt.config.ToolConfig;
import cn.sudt.deploy.scheme.CommandLine;
import cn.sudt.lang.io.CryptoUtils;
import cn.sudt.lang.io.ObjectStream;
import cn.sudt.ui.panel.CmdTablePanel;
/**
 * 导入命令对话框
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class CmdImportDialog extends FileInputDialog {
	
	private CmdTablePanel cmdTablePanel = null;
	
	public CmdImportDialog(CmdTablePanel cmdTablePanel) {
		super();
		this.cmdTablePanel = cmdTablePanel;
		setTitle(ToolConfig.i18.getProperty("sudt.command.list.import"));
		setVisible(true);
	}

	@Override
	protected String sureExecute() throws Exception {
		File file = new File(filePathField.getText());
		List<?> objs = null;

		if (encryptCheckBox.isSelected()) {
			String objStr = new CryptoUtils().decryptFileToString(
					file, passwordField.getText());
			if (StringUtils.isBlank(objStr)) {
				throw new Exception(ToolConfig.i18.getProperty(
					"sudt.file.password.error"));
			} else {
				objs = readObject(objStr);
			}
		} else {
			objs = new ObjectStream().readObject(CommandLine.class, file);
		}
		
		for (Object obj : objs) {
			CommandLine cmd = (CommandLine) obj;
			cmdTablePanel.getTableUtils().appendRow(
					cmdTablePanel.getTable(), 
					cmd.getCommand(), 
					cmd.isRetry(), 
					cmd.getLazy());
		}
		
		if (objs.size() > 0) {
			cmdTablePanel.getCmdExportBtn().setEnabled(true);
		}
		return null;
	}

	private List<CommandLine> readObject(String objStr) {
		List<CommandLine> objs = new ArrayList<CommandLine>();
		String[] hosts = objStr.split("\r\n");
		for (String host : hosts) {
			String[] args = host.split("\n");
			CommandLine cmd = new CommandLine(args[0], 
				Boolean.parseBoolean(args[1]), 
				Integer.parseInt(args[2]));
			objs.add(cmd);
		}
		return objs;
	}

	@Override
	public AbstractDialog getDialog() {
		return this;
	}
}
