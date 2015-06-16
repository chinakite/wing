/**
 * 
 */
package com.ideamoment.wing.core;

/**
 * @author Chinakite Zhang
 *
 */
public class MethodAfterSetterInterceptor4Test implements
		MethodAfterInterceptor {

	/* (non-Javadoc)
	 * @see com.ideamoment.wing.core.MethodAfterInterceptor#after()
	 */
	public void after() {
		Cache4Test.getCache().put("afterSetter", "MethodBeforeInterceptor4Test");
	}

	/* (non-Javadoc)
	 * @see com.ideamoment.wing.core.MethodInterceptorAdapter#adapt(java.lang.String)
	 */
	public boolean adapt(String methodName) {
		return false;
	}

}
