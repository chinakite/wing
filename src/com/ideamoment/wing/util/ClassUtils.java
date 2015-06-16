/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.util;

import java.util.HashMap;

import com.ideamoment.wing.exception.WingException;

/**
 * 类文件相关信息的工具类
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public class ClassUtils {
	/**
	 * Java中类或接口类型（所有非原始类型）在.class文件中是都是以内部名称(Internal Name)
	 * 来表示的。类的内部名称其实就是类的全名，但是要把所有的.替换为反斜线。
	 * 例如，<b>String</b>的内部名称是<b>java/lang/String</b>
	 * 
	 * @param clazz 类定义
	 * @return 类的Internal Name.
	 */
	public static String getInternalName(Class clazz) {
		return clazz.getCanonicalName().replaceAll("\\.", "/");
	}
	
	/**
	 * Java .class文件中的每个数据类型都有一套固定的描述格式，称为类型描述符（Type Descriptor）
	 * 描述符的规则是：
	 * <ul>
	 * 	<li>原始类型 - 使用描述符映射表中的定义，如<b>boolean</b>的描述符是<b>Z</b></li>
	 *  <li>类或接口类型 - 使用<b>L + 类或接口的内部名称 + ;</b>，如<b>java.lang.String</b>的描述符
	 *      是<b>Ljava/lang/String;</b>
	 *  </li>
	 *  <li>数组类型 - 使用<b>[ + 元素的类型描述符</b>，如<b>int[]</b>的描述符是<b>[I</b>，
	 *      <b>java.lang.String[]</b>的描述符是<b>[Ljava/lang/String;</b>
	 *  </li>
	 * </ul>
	 * 
	 * @param clazz 类型
	 * @return 类型的描述符（Type Descriptor）
	 */
	public static String getTypeDescriptor(Class clazz) {
		return getTypeDescriptor(clazz.getCanonicalName());
	}
	
	/**
	 * Java .class文件中的每个数据类型都有一套固定的描述格式，称为类型描述符（Type Descriptor）
	 * 描述符的规则是：
	 * <ul>
	 * 	<li>原始类型 - 使用描述符映射表中的定义，如<b>boolean</b>的描述符是<b>Z</b></li>
	 *  <li>类或接口类型 - 使用<b>L + 类或接口的内部名称 + ;</b>，如<b>java.lang.String</b>的描述符
	 *      是<b>Ljava/lang/String;</b>
	 *  </li>
	 *  <li>数组类型 - 使用<b>[ + 元素的类型描述符</b>，如<b>int[]</b>的描述符是<b>[I</b>，
	 *      <b>java.lang.String[]</b>的描述符是<b>[Ljava/lang/String;</b>
	 *  </li>
	 * </ul>
	 * 
	 * @param type 类型
	 * @return 类型的描述符（Type Descriptor）
	 */
	public static String getTypeDescriptor(String type) {
		int arrayDimension = getArrayDimensions(type);		//取得是几维的数组
		if(arrayDimension > 0) {
			String result = "";
			//几维数组就会加几个“[”在前面
			for(int i=0; i<arrayDimension; i++) {		
				result = result + "[";
			}
			result = result + getNotArrayTypeDescriptor(type.substring(0, type.indexOf("[")));
			return result;
		}else{
			return getNotArrayTypeDescriptor(type);
		}
	}
	
	/**
	 * 非数组类型获取类型描述符
	 * 
	 * @param type 字符串表示的非数组类型
	 * @return 类型描述符
	 */
	private static String getNotArrayTypeDescriptor(String type) {
		String result = typeDescriptorCache.get(type);
		if(result != null) {
			return result;
		}else{
			return "L" + type.replaceAll("\\.", "/") + ";";
		}
	}
	
	/**
	 * 获取一个字符串表示的类型是几维的数组。例如：
	 * <ul>
	 *   <li>int - 非数组，返回0</li>
	 *   <li>int[] - 一维数组，返回1</li>
	 *   <li>int[][][] - 三维数组，返回3</li>
	 *   <li>int[][[] - 不合法数组，抛出WingException异常</li>
	 *   <li>int] - 不合法数组，抛出WingException异常</li>
	 *   <li>int[][[]] - 不合法数组，抛出WingException异常</li>
	 * </ul>
	 * 
	 * @param type 字符串表示的类型。
	 * @return 数组的维数
	 */
	public static int getArrayDimensions(String type) {
		int result = 0;
		if(type.indexOf("[") > -1 || type.indexOf("]") > -1) {		//类型中有"["或"]"
			if(type.indexOf("[") > -1 && type.indexOf("]") > -1) {		//类型中同时有"["或"]"
				int firstLeftSquarePos = type.indexOf("[");
				int firstRightSquarePos = type.indexOf("]");
				if(firstRightSquarePos < firstLeftSquarePos) {		//如果第一个右括号在左括号前，则为非法数组
					throw new WingException(type + " is a invalidate array type.");
				}else{
					for(int i=firstLeftSquarePos; i<type.length(); i=i+2) {
						if(i == type.length() - 1) {
							if(type.charAt(i) == '[') {		//如果以左括号结尾，则为非法数组
								throw new WingException(type + " is a invalidate array type.");
							}
						}
						if(type.charAt(i+1) == ']'){
							result++;
						}else{		//如果只有左括号后面跟的不是右括号，则为非法数组
							throw new WingException(type + " is a invalidate array type.");
						}
					}
					return result;
				}
			}else{		//如果只有左括号或只有右括号，则为非法数组
				throw new WingException(type + " is a invalidate array type.");
			}
		}else{
			return result;
		}
	}
	
	//----------------------------------
	// 原始类型描述符映射表。
	//----------------------------------
	private static HashMap<String, String> typeDescriptorCache = new HashMap<String, String>();
	static {
		typeDescriptorCache.put("byte", "B");
		typeDescriptorCache.put("char", "C");
		typeDescriptorCache.put("double", "D");
		typeDescriptorCache.put("float", "F");
		typeDescriptorCache.put("int", "I");
		typeDescriptorCache.put("long", "J");
		typeDescriptorCache.put("short", "S");
		typeDescriptorCache.put("boolean", "Z");
	}
}
