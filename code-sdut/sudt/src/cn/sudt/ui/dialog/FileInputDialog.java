package cn.sudt.ui.dialog;

import java.awt.GridLayout;
import java.io.File;

import cn.sudt.config.ToolConfig;
import cn.sudt.ui.common.MessageType;
import cn.sudt.ui.common.UiConst;
/**
* 文件导入对话框模板
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public abstract class FileInputDialog extends FileStreamDialog {

	public FileInputDialog() {
		super(UiConst.DIALOG_IMPORT);
	}

	@Override
	protected boolean validates() {
		File file = new File(filePathField.getText());
		if (!file.exists()) {
			showInfo(MessageType.error, String.format(
				ToolConfig.i18.getProperty("sudt.file.null"), 
				file.getAbsolutePath()));
			
			return false;
		}
		return true;
	}
	
	@Override
	protected void setLayout() {
		labelPanel.setLayout(new GridLayout(3, 1));
		contextPanel.setLayout(new GridLayout(3, 1));
		btnPanel.setLayout(new GridLayout(3, 1));
		super.setLayout();
	}

}
