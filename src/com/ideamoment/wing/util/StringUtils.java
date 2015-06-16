/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.util;

/**
 * 字符串处理工具类。
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public class StringUtils {
	/**
	 * 将字符串的首字母变为大写
	 * 
	 * @param original 原始字符串
	 * @return 替换了首字母的字符串，如果首字母已是大写则不变。
	 */
	public static String toTitleCase(String original) {
		if(original == null)
			return null;
		return Character.toTitleCase(original.charAt(0)) + original.substring(1);
	}
}
