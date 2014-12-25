package cn.tool.ui.dialog;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;

import cn.tool.config.ToolConfig;
import cn.tool.ui.resour.IconContainer;
import cn.tool.ui.resour.SystemConst;
/**
* 文件导入对话框模板
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public abstract class FileInputDialog extends FileStreamDialog {

	public FileInputDialog() {
		super(SystemConst.DIALOG_IMPORT);
	}

	@Override
	protected boolean validates() {
		File file = new File(filePathField.getText());
		if (!file.exists()) {
			showInfo(IconContainer.icon_error, Color.RED, String.format(
				ToolConfig.i18.getProperty("soft.deploy.tool.file.null"), 
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
