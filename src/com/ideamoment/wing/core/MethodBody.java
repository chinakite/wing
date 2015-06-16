/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.core;

import org.objectweb.asm.MethodVisitor;

/**
 * 向类动态增加一个方法时，方法体的回调接口。
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public interface MethodBody {
	
	/**
	 * 方法体
	 */
	public void body(MethodVisitor methodVisitor);
}
