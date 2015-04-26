package cn.sudt.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.sudt.config.ToolConfig;
import cn.sudt.deploy.scheme.HostInfo;
import cn.sudt.lang.io.CryptoUtils;
import cn.sudt.lang.io.ObjectStream;
import cn.sudt.ui.panel.HostTablePanel;
/**
* 导入主机对话框
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public class HostImportDialog extends FileInputDialog {

	private HostTablePanel hostTablePanel;
	
	public HostImportDialog(HostTablePanel hostTablePanel)  {
		super();
		this.hostTablePanel = hostTablePanel;
		setTitle(ToolConfig.i18.getProperty("sudt.host.list.import"));
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
			objs = new ObjectStream().readObject(HostInfo.class, file);
		}
		
		if (objs.size() > 0) {
			hostTablePanel.getHostExportBtn().setEnabled(true);
		}
		
		return batchInsertHost(objs);
	}

	/**
	 * 向主机列表批量插入
	 * 
	 * @param objs
	 * @return 返回去重信息
	 */
	private String batchInsertHost(List<?> objs) {
		int rowCount = hostTablePanel.getTable().getRowCount();
		
		int repeatCount = 0;
		for (Object obj : objs) {
			HostInfo host = (HostInfo) obj;
			/*
			 * 导入时，过滤重复的ip
			 */
			int i = 0;
			for (; i < rowCount; i++) {
				if (hostTablePanel.getTable().getValueAt(i, 0).equals(host.getIp())) {
					repeatCount++;
					break;
				}
			}
			
			if (rowCount == i) {
				hostTablePanel.getTableUtils().appendRow(
						hostTablePanel.getTable(), 
						host.getIp(), 
						host.getUserName(), 
						host.getPassWord());
			}	
		} 
		
		if (repeatCount > 0) {
			return String.format(ToolConfig.i18.getProperty(
				"sudt.host.list.repeat"), repeatCount);
		} else {
			return null;
		}
	}

	/**
	 * 将JSON串解析成HostInfo实例
	 * 
	 * @param objStr
	 * @return
	 */
	private List<HostInfo> readObject(String objStr) {
		List<HostInfo> objs = new ArrayList<HostInfo>();
		String[] hosts = objStr.split("\r\n");
		for (String host : hosts) {
			String[] args = host.split("\n");
			HostInfo hostInfo = new HostInfo(
				args[0], Integer.parseInt(args[1]), args[2], args[3]);
			objs.add(hostInfo);
		}
		return objs;
	}

	@Override
	public AbstractDialog getDialog() {
		return this;
	}

}
