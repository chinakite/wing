/**
 * 
 */
package com.ideamoment.wing.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Admin
 *
 */
public class Cache4Test {
	
	private static Map<String, String> cache = new HashMap<String, String>();
	
	private Cache4Test() {
	}
	
	public static Map<String, String> getCache() {
		return cache;
	}
}
