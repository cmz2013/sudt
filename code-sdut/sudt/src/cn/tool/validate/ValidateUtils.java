package cn.tool.validate;

import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;

import cn.tool.config.ToolConfig;
import cn.tool.lang.ip.Ipv4Utils;
import cn.tool.validate.annotation.Validations.Type;
import cn.tool.validate.model.DataModel;

/**
 * 数据验证
 * 
 * @author chongming
 *
 */
public class ValidateUtils {

	public static boolean validate(
			List<DataModel> models) throws Exception {
		
		boolean validateResult = true;
		for (final DataModel model : models) {
			if (Type.ip.equals(model.getType())) {
				if (!validateIpv4(model) || !validateIpv6(model)) {
					validateResult = false;
				}
			} else if (Type.text.equals(model.getType())) {
				Pattern pattern = Pattern.compile(model.getRegular());
				Matcher matcher = pattern.matcher(model.getValue());
				if (!matcher.matches()) {
					setValidateInfo(model.getInfoLabel(), 
							model.getInfo());
					validateResult = false;
				}
			}
		}
		return validateResult;
	}

	private static boolean validateIpv6(DataModel dataModel) {
		return true;
	}

	/**
	 * 验证IPv4
	 * 
	 * @param dataModel
	 * @return
	 * @throws Exception
	 */
	private static boolean validateIpv4(DataModel dataModel) throws Exception {
		if (StringUtils.isBlank(dataModel.getValue())) {
			setValidateInfo(dataModel.getInfoLabel(), 
					ToolConfig.i18.getProperty("soft.deploy.tool.host.ip.null"));
			return false;
		} else if (!Ipv4Utils.checkIpFormat(dataModel.getValue())) {
			setValidateInfo(dataModel.getInfoLabel(), 
					ToolConfig.i18.getProperty("soft.deploy.tool.host.ip.error"));
			return false;
		} else if (!Ipv4Utils.isHostIP(dataModel.getValue())) {
			setValidateInfo(dataModel.getInfoLabel(), 
					ToolConfig.i18.getProperty("soft.deploy.tool.host.ip.unused"));
			return false;
		}
		return true;
	}

	private static void setValidateInfo(JLabel label, String tipText) {
		label.setText(" " + tipText);
		label.setForeground(Color.RED);
	}

}
