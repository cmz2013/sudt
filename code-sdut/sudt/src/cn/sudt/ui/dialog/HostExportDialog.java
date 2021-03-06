package cn.sudt.ui.dialog;

import java.util.List;

import cn.sudt.config.ToolConfig;
import cn.sudt.deploy.scheme.HostInfo;
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
		setTitle(ToolConfig.i18.getProperty("sudt.host.list.export"));
		setVisible(true);
	}

	@Override
	public AbstractDialog getDialog() {
		return this;
	}

}
