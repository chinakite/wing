/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.core;

/**
 * 方法拦截的适配器接口，用于根据方法属性决定是否需要拦截。
 * 
 * @author Chinakite Zhang
 * @version 20100919
 * @since 0.1
 */
public interface MethodInterceptorAdapter {
	/**
	 * 根据方法名适配
	 * 
	 * @param methodName 方法名
	 * @return true - 进行拦截；false - 不进行拦截
	 */
	public boolean adapt(String methodName);
}
