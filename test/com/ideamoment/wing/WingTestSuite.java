/**
 * 
 */
package com.ideamoment.wing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ideamoment.wing.core.TestWingClass;
import com.ideamoment.wing.util.TestClassUtils;

/**
 * @author Chinakite Zhang
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestWing.class,
	TestClassUtils.class,
	TestWingClass.class
})
public class WingTestSuite {

}
