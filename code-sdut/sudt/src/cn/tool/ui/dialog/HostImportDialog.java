package cn.tool.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import org.apache.commons.lang.StringUtils;

import cn.tool.config.ToolConfig;
import cn.tool.deploy.scheme.HostInfo;
import cn.tool.lang.io.CryptoUtils;
import cn.tool.lang.io.ObjectStream;
import cn.tool.lang.swing.TableUtils;
import cn.tool.ui.panel.HostTablePanel;
/**
* 导入主机对话框
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public class HostImportDialog extends FileInputDialog {

	private HostTablePanel tablePanel;
	
	public HostImportDialog(HostTablePanel tablePanel)  {
		super();
		this.tablePanel = tablePanel;
		setTitle(ToolConfig.i18.getProperty("soft.deploy.tool.host.list.import"));
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
			objs = ObjectStream.readObject(HostInfo.class, file);
		}
		
		if (objs.size() > 0) {
			tablePanel.getHostExportBtn().setEnabled(true);
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
		int rowCount = tablePanel.getTable().getRowCount();
		
		int repeatCount = 0;
		for (Object obj : objs) {
			HostInfo host = (HostInfo) obj;
			/*
			 * 导入时，过滤重复的ip
			 */
			int i = 0;
			for (; i < rowCount; i++) {
				if (tablePanel.getTable().getValueAt(i, 0).equals(host.getIp())) {
					repeatCount++;
					break;
				}
			}
			
			if (rowCount == i) {
				TableUtils.appendRow(tablePanel.getTable(), host.getIp(), 
					host.getUserName(), host.getPassWord());
			}	
		} 
		
		if (repeatCount > 0) {
			return String.format(ToolConfig.i18.getProperty(
				"soft.deploy.tool.host.list.repeat"), repeatCount);
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
				args[0], args[1], args[2]);
			objs.add(hostInfo);
		}
		return objs;
	}

	@Override
	protected JDialog getDialog() {
		return this;
	}

}
