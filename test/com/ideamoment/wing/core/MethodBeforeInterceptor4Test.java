/**
 * 
 */
package com.ideamoment.wing.core;

/**
 * @author Chinakite Zhang
 *
 */
public class MethodBeforeInterceptor4Test implements MethodBeforeInterceptor {

	/* (non-Javadoc)
	 * @see com.ideamoment.wing.core.MethodBeforeInterceptor#before()
	 */
	public void before() {
		Cache4Test.getCache().put("before", "MethodBeforeInterceptor4Test");
	}

	public boolean adapt(String methodName) {
		return true;
	}

}
