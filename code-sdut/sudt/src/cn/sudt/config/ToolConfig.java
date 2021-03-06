package cn.sudt.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.InputSource;

import sun.awt.AppContext;
import cn.sudt.lang.io.PropertyUtils;
/**
 * 工具配置
 * 
 * @author chongming
 *
 */
public class ToolConfig {
	private static PropertyUtils proUtils = new PropertyUtils();
	
	public static Properties i18;
	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String LOG_DIR = USER_DIR + "/log";
	
	public static DeplogConfig deploy;
	public static UserConfig user;
	
	static {
		try {
			initToolConfig();
			initI18Config();
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					null, e.getMessage());
			System.exit(-1);
		}
	}
	
	private static void initI18Config() throws Exception {
		if ("en".equalsIgnoreCase(user.getLanguage())) {
			i18 = proUtils.getProperties(System.getProperty(
					"user.dir") + "/i18/tool_des_en.properties");
			Locale.setDefault(Locale.US);
			AppContext.getAppContext().put("JComponent.defaultLocale", Locale.US);
		} else if ("zh".equalsIgnoreCase(user.getLanguage())) {
			i18 = proUtils.getProperties(System.getProperty(
					"user.dir") + "/i18/tool_des_zh.properties");
			Locale.setDefault(Locale.CHINA);
			AppContext.getAppContext().put("JComponent.defaultLocale", Locale.CHINA);
		/**
		 * According to the locale initialization i18 file
		 */
		} else if ("\u4e2d\u6587".equals(Locale.getDefault().getDisplayLanguage())) {
			i18 = proUtils.getProperties(System.getProperty(
					"user.dir") + "/i18/tool_des_zh.properties");
		} else {
			i18 = proUtils.getProperties(System.getProperty(
						"user.dir") + "/i18/tool_des_en.properties");
		}
	}

	private static void initToolConfig() throws Exception {
		File configFile = new File(USER_DIR + "/tool_config.xml");
		Digester digester = DigesterLoader.createDigester(
				new InputSource(new FileInputStream(USER_DIR + "/cfg/tool_config_rule.xml")));
		
		ToolConfig toolConfig = new ToolConfig();
		digester.push(toolConfig);
		digester.parse(configFile);
	}

	/**
	 * Digerter在解析配置文件时会调用该方法
	 * 
	 * @param deployConfig
	 */
	public void setDeployConfig(DeplogConfig deployConfig) {
		ToolConfig.deploy = deployConfig;
	}

	public void setUserConfig(UserConfig user) {
		ToolConfig.user = user;
	}
	
}
