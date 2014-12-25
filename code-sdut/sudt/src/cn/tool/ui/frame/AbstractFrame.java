package cn.tool.ui.frame;

import javax.swing.JFrame;

import cn.tool.config.ToolConfig;
import cn.tool.ui.resour.IconContainer;
import cn.tool.ui.resour.SystemConst;
/**
* 窗口模板
*
* @author chongming
*
*/
@SuppressWarnings("serial")
public abstract class AbstractFrame extends JFrame {
	
	public AbstractFrame() {
		super();
		init();
	}
	
	private void init() {
		setTitle(ToolConfig.i18.getProperty("soft.deploy.tool.title"));
		setSize(SystemConst.FRAME_WIDTH, SystemConst.FRAME_HEIGHT);
		setIconImage((IconContainer.icon_item.getImage()));
	}
}
