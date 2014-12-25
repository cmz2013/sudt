package cn.tool.ui.panel;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import cn.tool.ui.resour.SystemConst;
/**
 * 窗口布局面板
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public class SplitPane extends JSplitPane {
	
	public SplitPane(JPanel northPanel, JPanel southPanel) {
		super(JSplitPane.VERTICAL_SPLIT);
		setLayout(northPanel, southPanel);
	}

	private void setLayout(JPanel northPanel, JPanel southPanel) {
		setDividerLocation(new Double(SystemConst.FRAME_HEIGHT/2.33).intValue());
		add(northPanel);
		add(southPanel);
		setEnabled(false);
	}

}
