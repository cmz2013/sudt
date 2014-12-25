package cn.tool.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import org.apache.commons.lang.StringUtils;

import cn.tool.config.ToolConfig;
import cn.tool.deploy.scheme.CommandLine;
import cn.tool.lang.io.CryptoUtils;
import cn.tool.lang.io.ObjectStream;
import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.panel.CmdTablePanel;
/**
 * 导入命令对话框
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class CmdImportDialog extends FileInputDialog {
	
	private CmdTablePanel tablePanel = null;
	
	public CmdImportDialog(CmdTablePanel tablePanel) {
		super();
		this.tablePanel = tablePanel;
		setTitle(ToolConfig.i18.getProperty("soft.deploy.tool.command.list.import"));
		setVisible(true);
	}

	@Override
	protected String sureExecute() throws Exception {
		File file = new File(filePathField.getText());
		List<?> objs = null;

		if (encryptCheckBox.isSelected()) {
			String objStr = CryptoUtils.decryptFileToString(
					file, passwordField.getText());
			if (StringUtils.isBlank(objStr)) {
				throw new Exception(ToolConfig.i18.getProperty(
					"soft.deploy.tool.file.password.error"));
			} else {
				objs = readObject(objStr);
			}
		} else {
			objs = ObjectStream.readObject(CommandLine.class, file);
		}
		
		for (Object obj : objs) {
			CommandLine cmd = (CommandLine) obj;
			TableUtils.appendRow(
					tablePanel.getTable(), 
					cmd.getCommand(), 
					cmd.isRetry(), 
					cmd.getLazy());
		}
		
		if (objs.size() > 0) {
			tablePanel.getCmdExportBtn().setEnabled(true);
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
	protected JDialog getDialog() {
		return this;
	}
}
