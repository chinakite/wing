/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.core;

/**
 * 方法执行后的回调接口。
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public interface MethodAfterInterceptor extends MethodInterceptorAdapter {
	
	/**
	 * 方法执行后的回调方法
	 */
	public void after();
}
