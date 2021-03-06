package cn.sudt.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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
			String objStr = null;
			try {
				objStr = new CryptoUtils().decryptFileToString(
						file, passwordField.getText());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (StringUtils.isBlank(objStr)) {
					throw new Exception(ToolConfig.i18.getProperty(
						"sudt.file.password.error"));
				} else {
					objs = readObject(objStr);
				}
			}
		} else {
			objs = new ObjectStream().readObject(HostInfo.class, file);
		}
		
		if (objs.size() > 0) {
			batchInsertHost(objs);
			hostTablePanel.getHostExportBtn().setEnabled(true);
		}
		return null;
	}

	/**
	 * 向主机列表批量插入
	 * 
	 * @param objs
	 * @return 返回去重信息
	 */
	private void batchInsertHost(List<?> objs) {
		int rowCount = hostTablePanel.getTable().getRowCount();
		
		for (Object obj : objs) {
			HostInfo host = (HostInfo) obj;
			/*
			 * 导入时，过滤重复的ip
			 */
			int i = 0;
			for (; i < rowCount; i++) {
				String ip = hostTablePanel.getTable().getValueAt(i, 0) + "";
				String port = hostTablePanel.getTable().getValueAt(i, 1) + "";
				
				if (ip.equals(host.getIp()) && port.equals(host.getPort() + "")) {
					int option = JOptionPane.showConfirmDialog(null, 
							String.format(ToolConfig.i18.getProperty("sudt.host.list.repeat"), 
									host.getIp() + ":" + host.getPort()),
							ToolConfig.i18.getProperty("sudt.title.tip"),
							JOptionPane.OK_CANCEL_OPTION);
					
					if (JOptionPane.OK_OPTION == option) {
						hostTablePanel.getTableUtils().updateRow(
								hostTablePanel.getTable(), i,
								host.getIp(), 
								host.getPort() + "",
								host.getUserName(), 
								host.getPassWord());
					}
					
					break;
				}
			}
			
			if (rowCount == i) {
				hostTablePanel.getTableUtils().appendRow(
						hostTablePanel.getTable(), 
						host.getIp(), 
						host.getPort(),
						host.getUserName(), 
						host.getPassWord());
			}	
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
			String[] args = host.split("\r\n");
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
