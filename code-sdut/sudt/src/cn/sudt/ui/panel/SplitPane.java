package cn.sudt.ui.panel;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import cn.sudt.ui.common.UIConst;
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
		setDividerLocation(new Double(UIConst.FRAME_HEIGHT/2.33).intValue());
		add(northPanel);
		add(southPanel);
		setEnabled(false);
	}

}
