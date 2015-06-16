/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.exception;

/**
 * Wing程序的主异常类。
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public class WingException extends RuntimeException {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7889366114258391203L;
	
	public WingException(String msg) {
		super(msg);
	}
	
	public WingException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public WingException(Throwable t) {
		super(t);
	}
	
}
