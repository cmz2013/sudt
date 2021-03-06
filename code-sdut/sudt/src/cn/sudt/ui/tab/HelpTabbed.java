package cn.sudt.ui.tab;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import cn.sudt.config.ToolConfig;
import cn.sudt.ui.common.IconContainer;
import cn.sudt.ui.common.UIConst;


/**
 * 主窗口-工具帮助Tab页
 * 
 * @author chongming
 *
 */
public class HelpTabbed {
	
	private JTree tree;
	
	private DefaultMutableTreeNode useHelpNode = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("sudt.help.use"), true);
	private DefaultMutableTreeNode hostList = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("sudt.host.list"));
	private DefaultMutableTreeNode cmdList = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("sudt.command.list"));
	private DefaultMutableTreeNode console = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("sudt.console"));
	private DefaultMutableTreeNode taskbar = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("sudt.taskbar"));
	private DefaultMutableTreeNode toolbar = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("sudt.toolbar"));
	
	private DefaultMutableTreeNode aboutToolNode = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("sudt.help.about"), true);
	private DefaultMutableTreeNode download = new DefaultMutableTreeNode(
			ToolConfig.i18.getProperty("sudt.help.code.download"));
	private DefaultMutableTreeNode contact = new DefaultMutableTreeNode(
			ToolConfig.i18.getProperty("sudt.help.contact"));
	private DefaultMutableTreeNode setup = new DefaultMutableTreeNode(
			ToolConfig.i18.getProperty("sudt.help.tool.setup"));
	
	private JSplitPane helpPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JPanel textPane = new JPanel();
	private JTextPane titlePane = new JTextPane();
	private JTextPane contextPane = new JTextPane();
	
	public HelpTabbed() throws Exception {
		setLayout();
		Map<String, String> nodeText = setTextInfo();
		setNodeAction(nodeText);
	}
	
	private void setNodeAction(final Map<String, String> nodeText) {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				String key = e.getPath().getLastPathComponent() + "";
				titlePane.setText(key);
				contextPane.setText(nodeText.get(key));
			}
		});
	}

	/**
	 * 设置文本信息，并且设置树节点点击事件，相应的行置为首行
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, String> setTextInfo() throws Exception {
        Map<String, String> treeIndex = new HashMap<>();
        
        treeIndex.put(useHelpNode.getUserObject() + "", 
        		ToolConfig.i18.getProperty("sudt.help.use.root"));
        treeIndex.put(hostList.getUserObject() + "", 
        		ToolConfig.i18.getProperty("sudt.help.host.list") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.host.list.function1") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.host.list.function2") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.host.list.function3"));
        
        treeIndex.put(cmdList.getUserObject() + "", 
        		ToolConfig.i18.getProperty("sudt.help.command.list") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.command.list.function1") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.command.list.function2") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.command.list.function3") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.command.list.function4") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.command.list.upload") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.command.list.download") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.command.list.function5"));

        treeIndex.put(console.getUserObject() + "", 
        		ToolConfig.i18.getProperty("sudt.help.console") + 
        		"\n" + ToolConfig.i18.getProperty("sudt.help.console.function1") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.console.function2") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.console.function3"));
       
        treeIndex.put(taskbar.getUserObject() + "", 
        		ToolConfig.i18.getProperty("sudt.help.taskbar") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.taskbar.function1") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.taskbar.function2"));
       
        
        treeIndex.put(toolbar.getUserObject() + "",
        		ToolConfig.i18.getProperty("sudt.help.toolbar") + 
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.add") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.insert") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.del") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.import") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.export") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.up") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.down") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.execute") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.stop") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.pause") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.continue") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.toolbar.log"));
        
        treeIndex.put(aboutToolNode.getUserObject() + "", 
        		ToolConfig.i18.getProperty("sudt.help.about.root"));
        treeIndex.put(download.getUserObject() + "", 
        		ToolConfig.i18.getProperty("sudt.help.code.download.url"));
        treeIndex.put(contact.getUserObject() + "", 
        		ToolConfig.i18.getProperty("sudt.help.contact.writer") + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.contact.email") + 
        		"\n" + ToolConfig.i18.getProperty("sudt.help.contact.qq"));
        
        treeIndex.put(setup.getUserObject() + "", 
        		String.format(ToolConfig.i18.getProperty(
						"sudt.help.tool.setup.tip1"), ToolConfig.USER_DIR) + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip2") + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip3") + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip4") + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip5") + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip6") + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip7") + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip8") + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip9") + 
				"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip10") + 
        		"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip11") +
        		"\n" + ToolConfig.i18.getProperty("sudt.help.tool.setup.tip12"));
        
        return treeIndex;
	}

	private void setLayout() {
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		useHelpNode.add(hostList);
		useHelpNode.add(cmdList);
		useHelpNode.add(console);
		useHelpNode.add(taskbar);
		useHelpNode.add(toolbar);
		useHelpNode.add(setup);
		
		aboutToolNode.add(download);
		aboutToolNode.add(contact);
		
		textPane.setLayout(new BorderLayout());
		Font titleFont = new Font(
				titlePane.getFont().getFontName(), 
				Font.BOLD, 
				titlePane.getFont().getSize() + 2);
		titlePane.setFont(titleFont);
		titlePane.setEditable(false);
		titlePane.setText(ToolConfig.i18.getProperty("sudt.help.use"));
		textPane.add(titlePane, BorderLayout.NORTH);
		
		contextPane.setEditable(false);
		contextPane.setText(ToolConfig.i18.getProperty("sudt.help.use.root"));
		textPane.add(contextPane, BorderLayout.CENTER);
		
		DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode();
		renderer.setLeafIcon(IconContainer.icon_help_item);
		renderer.setOpenIcon(IconContainer.icon_help);
		renderer.setClosedIcon(IconContainer.icon_help);
		treeRoot.add(useHelpNode);
		treeRoot.add(aboutToolNode);
		tree = new JTree(treeRoot);
		tree.setSelectionPath(tree.getPathForRow(1));
		tree.setRootVisible(false);
		tree.setCellRenderer(renderer);
		helpPane.setDividerSize(1);
		helpPane.setDividerLocation(UIConst.FRAME_WIDTH/4);
		helpPane.add(new JScrollPane(tree));
		helpPane.add(textPane);
		helpPane.setEnabled(false);
	}

	public JSplitPane getHelpPane() {
		return helpPane;
	}
	
}
