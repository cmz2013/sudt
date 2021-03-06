package cn.sudt.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定需要验证的文本框
 * 
 * @author chongming
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validations {
	/*
	 * 注解属性的类型可以为：基本数据类型，String类型，Class
	 * 类型，enum类型，annotation类型,前面所有的数组类型
	 */
	
	/**
	 * 文本框数据类型
	 */
	 public static enum Type {
		 ip, port, text
	 }
	
	 /**
	  * 如果文本框数据为text类型，需要指定正则表达式
      * 如果文本框数据为ip、port、int类型，不需要指定正则表达式和信息key
	  */ 
	public String regular() default "";
	/**
	 * 验证失败提示信息Key
	 */
	public String info() default "";
	/**
	 * 显示验证信息的Label
	 */
	public String getInfoLabel();
	
	public Type type();

}
