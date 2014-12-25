package cn.tool.validate.model;

import javax.swing.JLabel;

import cn.tool.validate.annotation.Validations.Type;
/**
 * 用于验证的数据模型
 * 
 * @author chongming
 *
 */
public class DataModel {
	private String value;
	private String regular;
	private String info;
	private JLabel infoLabel;
	private Type type;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRegular() {
		return regular;
	}
	public void setRegular(String regular) {
		this.regular = regular;
	}
	public JLabel getInfoLabel() {
		return infoLabel;
	}
	public void setInfoLabel(JLabel infoLabel) {
		this.infoLabel = infoLabel;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}
