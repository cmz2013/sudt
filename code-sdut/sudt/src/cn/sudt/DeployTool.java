package cn.sudt;

import javax.swing.JOptionPane;

import cn.sudt.ui.frame.MainFrame;
/**
 * 软件部署工具
 * 
 * @author chongming
 *
 */
public class DeployTool {
	
	public static void main(String[] args) {
		try {
			new MainFrame();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(-1);
		}
	}

}
