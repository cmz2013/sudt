package cn.tool.ui.dialog;

import java.util.List;

import javax.swing.JDialog;

import cn.tool.config.ToolConfig;
import cn.tool.deploy.scheme.HostInfo;
/**
* 导出主机对话框
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public class HostExportDialog extends FileOutputDialog {
	
	public HostExportDialog(List<HostInfo> hosts) {
		super(hosts);
		setTitle(ToolConfig.i18.getProperty("soft.deploy.tool.host.list.export"));
		setVisible(true);
	}

	@Override
	protected JDialog getDialog() {
		return this;
	}

}
