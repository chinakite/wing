/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing;

import com.ideamoment.wing.core.WingClass;

/**
 * Wing程序的入口程序，主要是对服务进行一些封装，提供一些方便使用静态方法。
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public class Wing {
	/**
	 * 创建一个类的子类。
	 * 
	 * @param clazz 父类
	 * @return 子类的描述，WingClass类型
	 */
	public static WingClass createSubClass(Class clazz) {
		WingClass wclass = new WingClass();
		wclass.setSuperClass(clazz);
		return wclass;
	}
}
