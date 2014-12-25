package cn.tool.config;
/**
 * 用户个性设置
 * 
 * @author chongming
 *
 */
public class UserConfig {
	/**
	 * 默认用户名
	 */
	private String defaultUser = "root";
	/**
	 * 完成更新后关闭对话框
	 */
	private boolean closeUpdate = true;
	/**
	 * 完成添加后关闭对话框
	 */
	private boolean closeAdd = false;
	/**
	 * 密码框显示密码
	 */
	private boolean showPassword = false;
	/**
	 * 语言
	 */
	private String language;
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public String getDefaultUser() {
		return defaultUser;
	}
	
	public void setDefaultUser(String defaultUser) {
		this.defaultUser = defaultUser;
	}
	
	public boolean isCloseUpdate() {
		return closeUpdate;
	}
	
	public void setCloseUpdate(boolean closeUpdate) {
		this.closeUpdate = closeUpdate;
	}
	
	public boolean isCloseAdd() {
		return closeAdd;
	}
	
	public void setCloseAdd(boolean closeAdd) {
		this.closeAdd = closeAdd;
	}
	
	public boolean isShowPassword() {
		return showPassword;
	}
	
	public void setShowPassword(boolean showPassword) {
		this.showPassword = showPassword;
	}
}
