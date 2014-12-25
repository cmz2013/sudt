package cn.tool.ui.tab;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import cn.tool.config.ToolConfig;
import cn.tool.ui.resour.IconContainer;
import cn.tool.ui.resour.SystemConst;


/**
 * 主窗口-工具帮助Tab页
 * 
 * @author chongming
 *
 */
public class HelpTabbed {
	private DefaultMutableTreeNode useHelpNode = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("soft.deploy.tool.help.use"), true);
	private DefaultMutableTreeNode hostList = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("soft.deploy.tool.host.list"));
	private DefaultMutableTreeNode cmdList = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("soft.deploy.tool.command.list"));
	private DefaultMutableTreeNode console = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("soft.deploy.tool.console"));
	private DefaultMutableTreeNode taskbar = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("soft.deploy.tool.taskbar"));
	private DefaultMutableTreeNode toolbar = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("soft.deploy.tool.toolbar"));
	
	private DefaultMutableTreeNode aboutToolNode = new DefaultMutableTreeNode(
				ToolConfig.i18.getProperty("soft.deploy.tool.help.about"), true);
	private DefaultMutableTreeNode download = new DefaultMutableTreeNode(
			ToolConfig.i18.getProperty("soft.deploy.tool.help.code.download"));
	private DefaultMutableTreeNode contact = new DefaultMutableTreeNode(
			ToolConfig.i18.getProperty("soft.deploy.tool.help.contact"));
	private DefaultMutableTreeNode setup = new DefaultMutableTreeNode(
			ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup"));
	private JTextPane textPane = new JTextPane();
	private JSplitPane helpPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JTree tree;
	
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
				textPane.setText(nodeText.get(key));
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
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.use.root"));
        treeIndex.put(hostList.getUserObject() + "", 
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.host.list") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.host.list.function1") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.host.list.function2") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.host.list.function3"));
        
        treeIndex.put(cmdList.getUserObject() + "", 
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.command.list") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.command.list.function1") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.command.list.function2") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.command.list.function3") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.command.list.function4") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.command.list.upload") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.command.list.download") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.command.list.function5"));

        treeIndex.put(console.getUserObject() + "", 
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.console") + 
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.console.function1") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.console.function2") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.console.function3"));
       
        treeIndex.put(taskbar.getUserObject() + "", 
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.taskbar") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.taskbar.function1") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.taskbar.function2"));
       
        
        treeIndex.put(toolbar.getUserObject() + "",
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar") + 
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.add") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.insert") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.del") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.import") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.export") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.up") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.down") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.execute") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.stop") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.pause") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.continue") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.toolbar.log"));
        
        treeIndex.put(aboutToolNode.getUserObject() + "", 
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.about.root"));
        treeIndex.put(download.getUserObject() + "", 
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.code.download") +
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.code.download.url"));
        treeIndex.put(contact.getUserObject() + "", 
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.contact") +
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.contact.writer") + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.contact.email") + 
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.contact.qq"));
        
        treeIndex.put(setup.getUserObject() + "", 
        		ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup") +
				"\n" + String.format(ToolConfig.i18.getProperty(
						"soft.deploy.tool.help.tool.setup.tip1"), ToolConfig.USER_DIR) + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip2") + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip3") + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip4") + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip5") + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip6") + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip7") + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip8") + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip9") + 
				"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip10") + 
        		"\n" + ToolConfig.i18.getProperty("soft.deploy.tool.help.tool.setup.tip11"));
        
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
		DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode();
		renderer.setLeafIcon(IconContainer.icon_help_item);
		renderer.setOpenIcon(IconContainer.icon_help);
		renderer.setClosedIcon(IconContainer.icon_help);
		treeRoot.add(useHelpNode);
		treeRoot.add(aboutToolNode);
		tree = new JTree(treeRoot);
		tree.setSelectionPath(tree.getPathForRow(1));
		textPane.setText(ToolConfig.i18.getProperty("soft.deploy.tool.help.use.root"));
		tree.setRootVisible(false);
		tree.setCellRenderer(renderer);
		helpPane.setDividerLocation(new Double(SystemConst.FRAME_WIDTH/3.8).intValue());
		helpPane.add(new JScrollPane(tree));
		JScrollPane jsp = new JScrollPane(textPane);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		helpPane.add(jsp);
		helpPane.setEnabled(false);
		textPane.setEditable(false);
	}

	public JSplitPane getHelpPane() {
		return helpPane;
	}
	
}
