package cn.sudt.lang.swing;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;

import cn.sudt.config.ToolConfig;
import cn.sudt.ui.common.UIConst;
import cn.sudt.ui.dialog.FileStreamDialog;
/**
 * 组件工厂 (工厂模式)
 * 
 * @author chongming
 *
 */
public class SwingFactory {
	
	/**
	 * 设置性监听器
	 * @param textBox
	 * @param label
	 * @param text
	 */
	public static void setMouseListener(final JTextComponent textBox, 
			final JLabel label, final String text) {
		
		textBox.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent mouseevent) {
				try {
					if (Color.RED.equals(label.getForeground())) {
						label.setText(text);
						label.setForeground(Color.BLACK);
					}
				} catch (Throwable e) {

				}
			}
			public void mouseReleased(MouseEvent mouseevent) { }
			public void mousePressed(MouseEvent mouseevent) { }
			public void mouseExited(MouseEvent mouseevent) { }
			public void mouseEntered(MouseEvent mouseevent) { }
		});
	}

	/**
	 * 创建按钮
	 * 
	 * @param tipKey
	 * @param icon
	 * @return
	 */
	public static JButton getButton(String tipKey, ImageIcon icon) {
		JButton btn = new JButton();
		if (null != icon) {
			btn.setToolTipText(ToolConfig.i18.getProperty(tipKey));
			btn.setIcon(icon);
		} else {
			btn.setText(ToolConfig.i18.getProperty(tipKey));
		}
		return btn;
	}
	
	/**
	 * 创建按钮
	 * 
	 * @param textKey
	 * @return
	 */
	public static JButton getButton(String textKey) {
		return getButton(textKey, null);
	}
	
	/**
	 * 创建菜单按钮
	 * 
	 * @param tipKey
	 * @param icon
	 * @return
	 */
	public static JMenuItem getMenuItem(String tipKey, ImageIcon icon) {
		JMenuItem btn = new JMenuItem();
		if (null != icon) {
			btn.setToolTipText(ToolConfig.i18.getProperty(tipKey));
			btn.setIcon(icon);
		} else {
			btn.setText(ToolConfig.i18.getProperty(tipKey));
		}
		return btn;
	}
	
	public static JMenuItem getMenuItem(String textKey) {
		return getMenuItem(textKey, null);
	}
	
	/**
	 * 创建JFileChooser
	 * 
	 * @return
	 */
	public static JFileChooser getFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return FileStreamDialog.FILE_SUFFIX_CFG;
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else if (f.getAbsolutePath().toLowerCase().endsWith(
						FileStreamDialog.FILE_SUFFIX_CFG)) {
					return true;
				}
				return false;
			}
		});
		return fileChooser;
	}

	/**
	 * 创建Table
	 * 
	 * @return
	 */
	public static JTable getTable() {
		@SuppressWarnings("serial")
		JTable table = new JTable() {
			@Override
			public String getToolTipText(MouseEvent e) {
				int row = this.rowAtPoint(e.getPoint());
				int col = this.columnAtPoint(e.getPoint());
				String tiptextString = null;
				if (row > -1 && col > -1) {
					Object value = this.getValueAt(row, col);
					if (null != value && !"".equals(value))
						tiptextString = value.toString();
				}
				return tiptextString;
			}
		};
		return table;
	}
	
	public static JTextField getTextField() {
		final JTextField textField = new JTextField(UIConst.TEXT_FIELD_COLUMNS);
		textField.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) { }
			public void mousePressed(MouseEvent arg0) { }
			public void mouseExited(MouseEvent arg0) { }
			public void mouseEntered(MouseEvent arg0) {
				if (StringUtils.isNotBlank(textField.getText())) {
					textField.setToolTipText(textField.getText());
				} else {
					textField.setToolTipText(null);
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) { }
		});
		return textField;
	}
}
