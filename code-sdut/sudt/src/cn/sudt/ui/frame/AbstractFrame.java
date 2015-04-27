package cn.sudt.ui.frame;

import javax.swing.JFrame;

import cn.sudt.config.ToolConfig;
import cn.sudt.ui.common.IconContainer;
import cn.sudt.ui.common.UiConst;
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
		setTitle(ToolConfig.i18.getProperty("sudt.title"));
		setSize(UiConst.FRAME_WIDTH, UiConst.FRAME_HEIGHT);
		setIconImage((IconContainer.icon_item.getImage()));
	}
}