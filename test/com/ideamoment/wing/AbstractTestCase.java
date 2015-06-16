/**
 * 
 */
package com.ideamoment.wing;

import org.junit.After;

import com.ideamoment.wing.cache.ClassBytesCache;

/**
 * 测试基类，用于定义每个测试用例执行前和执行后的统一操作
 * 
 * @author Chinakite Zhang
 * @version 2010-11-19
 * @since 0.1
 */
public class AbstractTestCase {
	@After
	public void clearCache() {
		ClassBytesCache.clear();
	}
}
