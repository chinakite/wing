/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.classloader;

/**
 * 自定义ClassLoader，用于在使用字节码生成新类以后加载类。
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public class WingClassLoader extends ClassLoader {
	
	/**
	 * 向JVM加载一个新的类。
	 * 
	 * @param name 类名
	 * @param b 字节码数组
	 * @return 类的定义，{@link Class}类型
	 */
	public Class defineClass(String name, byte[] b) {
		return defineClass(name, b, 0, b.length);
	}
	
}
