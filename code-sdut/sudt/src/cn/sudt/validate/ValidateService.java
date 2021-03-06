package cn.sudt.validate;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;

import cn.sudt.config.ToolConfig;
import cn.sudt.lang.ip.Ipv4Utils;
import cn.sudt.lang.swing.SwingFactory;
import cn.sudt.ui.dialog.AbstractDialog;
import cn.sudt.validate.annotation.Validations;
import cn.sudt.validate.annotation.Validations.Type;
import cn.sudt.validate.model.DataModel;

/**
 * 数据验证
 * 
 * @author chongming
 *
 */
public class ValidateService {
	
	private Map<JLabel, String> labelMap = new HashMap<JLabel, String>();

	public boolean validate(AbstractDialog dialog) throws Exception {
		
		boolean validateResult = true;
		List<DataModel> models = getDataModels(dialog);
		
		for (final DataModel model : models) {
			if (Type.ip.equals(model.getType())) {
				if (!validateIpv4(model) || !validateIpv6(model)) {
					validateResult = false;
				}
			} else if (Type.text.equals(model.getType())) {
				Pattern pattern = Pattern.compile(model.getRegular());
				Matcher matcher = pattern.matcher(model.getValue() + "");
				if (!matcher.matches()) {
					setValidateInfo(model.getInfoLabel(), 
							model.getInfo());
					validateResult = false;
				}
			} else if (Type.port.equals(model.getType())) {
				if (!validatePort(model)) {
					validateResult = false;
				}
			}
		}
		return validateResult;
	}

	/**
	 * 验证端口号
	 * @param dataModel
	 * @return
	 */
	private boolean validatePort(DataModel dataModel) {
		try {
			String portStr = dataModel.getValue();
			
			if (StringUtils.isBlank(portStr)) {
				setValidateInfo(dataModel.getInfoLabel(), 
						ToolConfig.i18.getProperty("sudt.host.port.null"));
				return false;
			}
			
			int port = Integer.parseInt(portStr);
			if (port < 0 || port > 65535) {
				setValidateInfo(dataModel.getInfoLabel(), 
						ToolConfig.i18.getProperty("sudt.host.port.error"));
				return false;
			}
		} catch (Exception e) {
			setValidateInfo(dataModel.getInfoLabel(), 
					ToolConfig.i18.getProperty("sudt.host.port.error"));
			return false;
		}
		
		return true;
	}

	private boolean validateIpv6(DataModel dataModel) {
		// TODO
		return true;
	}

	/**
	 * 验证IPv4
	 * 
	 * @param dataModel
	 * @return
	 * @throws Exception 
	 */
	private boolean validateIpv4(DataModel dataModel) throws Exception {
		Ipv4Utils ipUtils = new Ipv4Utils();
		String hostIp = dataModel.getValue();
		if (StringUtils.isBlank(hostIp)) {
			setValidateInfo(dataModel.getInfoLabel(), 
					ToolConfig.i18.getProperty("sudt.host.ip.null"));
			return false;
		} else if (!ipUtils.checkIpFormat(hostIp)) {
			setValidateInfo(dataModel.getInfoLabel(), 
					ToolConfig.i18.getProperty("sudt.host.ip.error"));
			return false;
		} else if (!ipUtils.isHostIP(hostIp)) {
			setValidateInfo(dataModel.getInfoLabel(), 
					ToolConfig.i18.getProperty("sudt.host.ip.unused"));
			return false;
		}
		return true;
	}

	/**
	 * 设置校验失败提示信息
	 * @param label
	 * @param tipText
	 */
	private void setValidateInfo(JLabel label, String tipText) {
		label.setText(" " + tipText);
		label.setForeground(Color.RED);
	}
	
	/**
	 * 封装数据模型DataModel
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * 
	 */
	private List<DataModel> getDataModels(AbstractDialog dialog) throws 
			IllegalArgumentException, IllegalAccessException, 
			InvocationTargetException, NoSuchMethodException, 
			SecurityException {
		
		List<DataModel> dataModels = new ArrayList<>();
		Class<?> clazz  = null;
		
		do {
			if (null == clazz) {
				clazz = dialog.getDialog().getClass();
			} else {
				clazz = clazz.getSuperclass();
			}
			
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(Validations.class)) {
					field.setAccessible(true);
					Validations valaAnno = field.getAnnotation(Validations.class);
					Object dataObj =  field.get(dialog.getDialog());
					
					if (null != dataObj) {
						if (dataObj instanceof JTextComponent) {
							JTextComponent textBox = (JTextComponent) dataObj;
							DataModel model = getTextBoxDataModels(dialog, textBox, valaAnno);
							if (null != model) {
								dataModels.add(model);
							}
						}
					}
					field.setAccessible(false);
				}
			}		
		} while(!AbstractDialog.class.equals(clazz));
		
		return dataModels;
	}
	
	/**
	 * 获取JTextComponent类型的 DataModels
	 * @param textBox
	 * @param valaAnno
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private DataModel getTextBoxDataModels(AbstractDialog dialog, 
			JTextComponent textBox, Validations valaAnno) throws 
			IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException, NoSuchMethodException, 
			SecurityException {
		
		boolean enabled = textBox.isEnabled();
		/*
		 * 组件不可用时不验证
		 */
		if (enabled) {
			JLabel label = (JLabel) dialog.getDialog().getClass().getMethod(
					valaAnno.getInfoLabel()).invoke(dialog.getDialog());
			
			if (!labelMap.containsKey(label)) {
				labelMap.put(label, label.getText());
				SwingFactory.setMouseListener(textBox, label, labelMap.get(label));
			}
			
			DataModel dataModel = new DataModel();
			dataModel.setInfoLabel(label);
			dataModel.setType(valaAnno.type());
			dataModel.setValue(textBox.getText());
			dataModel.setInfo(ToolConfig.i18.getProperty(valaAnno.info()));
			dataModel.setRegular(valaAnno.regular());
			return dataModel;
		}
		return null;
	}
	
	/**
	 * 清空验证失败提示信息
	 */
	public void setLabelDefaultText() {
		for (JLabel label : labelMap.keySet()) {
			if (Color.RED.equals(label.getForeground())) {
				label.setText(labelMap.get(label));
				label.setForeground(Color.BLACK);
			}
		}
	}

}
