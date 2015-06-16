/**
 * 
 */
package com.ideamoment.wing.core;

/**
 * @author Admin
 *
 */
public class MethodAfterInterceptor4Test implements MethodAfterInterceptor {

	/* (non-Javadoc)
	 * @see com.ideamoment.wing.core.MethodAfterInterceptor#after()
	 */
	public void after() {
		Cache4Test.getCache().put("after", "MethodAfterInterceptor4Test");
	}

	public boolean adapt(String methodName) {
		return true;
	}

}
