package cn.tool.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import cn.tool.lang.swing.ComFactory;
import cn.tool.ui.resour.IconContainer;
/**
 * table模板
 * 
 * @author chongming
 *
 */
@SuppressWarnings("serial")
public abstract class TablePanel extends JPanel {
	protected JTable table = ComFactory.getTable();
	protected JScrollPane tableScrollPanel = new JScrollPane(table);
	protected boolean tableEnabled = true;
	
	private JPanel btnPanel = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			g.drawImage(IconContainer.icon_item.getImage(), 0, 0,
				IconContainer.icon_item.getIconWidth(),
				IconContainer.icon_item.getIconHeight(),
				IconContainer.icon_item.getImageObserver());
		}
	};
	
	protected JButton addBtn = ComFactory.getButton(
			"soft.deploy.tool.button.add", IconContainer.icon_add);
	protected JButton delBtn = ComFactory.getButton(
			"soft.deploy.tool.button.delete", IconContainer.icon_del);
	
	public TablePanel(DefaultTableModel model) {
		super();
		table.setModel(model);
		setLayout();
		setListener();
	}
	
	/**
	 * private，禁止子类重写
	 */
	private void setListener() {
		addBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				setAddBtnAction();
			}
		});
		
		delBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				if (setDelBtnAction()) {
					delBtn.setEnabled(false);
				}
			}
		});
		
		table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent mouseevent) { }
			public void mousePressed(MouseEvent mouseevent) { }
			public void mouseExited(MouseEvent mouseevent) { }
			public void mouseEntered(MouseEvent mouseevent) { }
			public void mouseClicked(MouseEvent mouseevent) {
				setTableMouseListener(mouseevent);
				if (table.getSelectedRow() >= 0) {
					delBtn.setEnabled(true);
				}
			}
		});
	}
	
	protected abstract boolean setAddBtnAction();
	protected abstract boolean setDelBtnAction();
	protected abstract void setTableMouseListener(MouseEvent mouseevent);

	private void setLayout() {
		table.getTableHeader().setEnabled(false);
		table.setBackground(new Color(214, 217, 223));
		/*
		 * 不自动调整列的宽度，使用滚动条
		 */
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		delBtn.setEnabled(false);
		
		GridLayout btnLayout = new GridLayout(11, 1);
		btnPanel.setLayout(btnLayout);
		
		Component[] hostBtns = { addBtn, delBtn };
		
		for (int i = 0; i < hostBtns.length; i++) {
			btnPanel.add(hostBtns[i]);
		}
		
		tableScrollPanel.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPanel.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		setLayout(new BorderLayout());
		add(tableScrollPanel, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.EAST);
	}
	
	public JTable getTable() {
		return table;
	}

	public JPanel getBtnPanel() {
		return btnPanel;
	}

	public JButton getAddBtn() {
		return addBtn;
	}

	public JButton getDelBtn() {
		return delBtn;
	}

	public boolean isTableEnabled() {
		return tableEnabled;
	}

	public void setTableEnabled(boolean tableEnabled) {
		this.tableEnabled = tableEnabled;
	}
	
}
