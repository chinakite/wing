/**
 * 
 */
package com.ideamoment.wing.core;

/**
 * @author Chinakite Zhang
 *
 */
public class MethodBeforeGetterInterceptor4Test implements
		MethodBeforeInterceptor {

	/* (non-Javadoc)
	 * @see com.ideamoment.wing.core.MethodBeforeInterceptor#before()
	 */
	public void before() {
		Cache4Test.getCache().put("beforeGetter", "MethodBeforeInterceptor4Test");
	}

	/* (non-Javadoc)
	 * @see com.ideamoment.wing.core.MethodInterceptorAdapter#adapt(java.lang.String)
	 */
	public boolean adapt(String methodName) {
		String pattern = "get\\w*";
		return methodName.matches(pattern);
	}
	
}
