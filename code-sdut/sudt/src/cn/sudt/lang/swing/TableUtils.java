package cn.sudt.lang.swing;

import java.awt.Component;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import cn.sudt.config.ToolConfig;
/**
 * table工具类
 * 
 * @author chongming
 *
 */
public class TableUtils {
	
	/**
	 * 隐藏表格指定的列
	 * @param table
	 * @param colIndex
	 */
	public void hideColumn(JTable table, int ... colIndex) {
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();
		for (int i = 0; i < colIndex.length; i++) {
			dcm.getColumn(colIndex[i]).setPreferredWidth(0);
			dcm.getColumn(colIndex[i]).setMinWidth(0);
			dcm.getColumn(colIndex[i]).setMaxWidth(0);
		}
	}
	
	/**
	 * 获取单元格不可编辑的DefaultTableModel
	 */
	@SuppressWarnings("serial")
	public DefaultTableModel getDefaultTableModel(String ...columns) {
		return new DefaultTableModel(null, columns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				/**
				 * 设置Table单元格不可编辑
				 */
	             return false;
	         }
		};
	}

	/**
	 * 向上或向下移动table选中的行
	 * 
	 * @param table
	 * @param isMoveUp
	 */
	public void moveRows(JTable table, boolean isMoveUp) {
		int[] rows = table.getSelectedRows();
		if (isMoveUp) {
			if (null != rows && rows.length > 0  && rows[0] > 0) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				ListSelectionModel selModel = new DefaultListSelectionModel();
				for(int i = 0; i < rows.length; i++) {
					model.moveRow(rows[i], rows[i], rows[i] - 1);
					selModel.addSelectionInterval(rows[i] - 1, rows[i] - 1);
					selModel.removeSelectionInterval(rows[i], rows[i]);
				}
				table.setSelectionModel(selModel);
			}
		} else {
			if (null != rows  && rows.length > 0 && (rows[rows.length - 1] < (table.getRowCount() - 1))) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				ListSelectionModel selModel = new DefaultListSelectionModel();
				for(int i = rows.length - 1; i >= 0; i--) {
					model.moveRow(rows[i], rows[i], rows[i] + 1);
					selModel.addSelectionInterval(rows[i] + 1, rows[i] + 1);
					selModel.removeSelectionInterval(rows[i], rows[i]);
				}
				table.setSelectionModel(selModel);
			}
		}
	}

	/**
	 * 追加行
	 * @param table
	 * @param values
	 */
	public void appendRow(JTable table, Object ...values) {
		((DefaultTableModel) table.getModel()).insertRow((
				(DefaultTableModel) table.getModel()).getRowCount(), values);
	}
	
	/**
	 * 插入行
	 * @param table
	 * @param insertRow
	 * @param values
	 */
	public void insertRow(JTable table, int insertRow, Object ...values) {
		((DefaultTableModel) table.getModel()).insertRow(insertRow, values);
	}

	/**
	 * 更新行
	 * @param table
	 * @param row
	 * @param values
	 */
	public void updateRow(JTable table, int row, Object ...values) {
		DefaultTableModel tableModel = ((DefaultTableModel) table.getModel());
		for (int i = 0; i < values.length; i++) {
			tableModel.setValueAt(values[i], row, i);
		}
	}
	
	/**
	 * 删除鼠标选中的行
	 * @param table
	 * @param infoKey
	 */
	public boolean deleteRow(JTable table, String infoKey) {
		int[] rows = table.getSelectedRows();
		if (null != rows && rows.length > 0) {
			
			int option = JOptionPane.showConfirmDialog(null,
					ToolConfig.i18.getProperty(infoKey), 
					ToolConfig.i18.getProperty("sudt.title.confirm"), 
					JOptionPane.WARNING_MESSAGE);
			
			if (JOptionPane.OK_OPTION == option) {
				DefaultTableModel model = ((DefaultTableModel) table.getModel());
				for (int i = 0; i < rows.length; i++) {
					model.removeRow(rows[i] - i);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据单元格内容计算指定列的最大宽度
	 * 
	 * @param table
	 * @param c
	 * @return
	 */
	private int getMaxWidthForCloumn(JTable table, int col) {
		int maxw = 0;
		for (int r = 0; r < table.getRowCount(); ++r) {
			TableCellRenderer renderer = table.getCellRenderer(r, col);
			Component comp = renderer.getTableCellRendererComponent(table,
					table.getValueAt(r, col), false, false, r, col);
			if (comp.getPreferredSize().width > maxw) {
				maxw = comp.getPreferredSize().width;
			}
		}
		return maxw;
	}
	 
	/**
	 * 根据单元格内容调整指定列的宽度
	 * 
	 * @param table
	 * @param col
	 */
	 public void setCloumnWidth(JTable table, int col) {
		 int width = getMaxWidthForCloumn(table, col);
		 table.getColumnModel().getColumn(col).setMinWidth(width);
	 }

}

