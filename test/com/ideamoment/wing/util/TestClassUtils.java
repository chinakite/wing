/**
 * 
 */
package com.ideamoment.wing.util;

import org.junit.Test;

import com.ideamoment.wing.AbstractTestCase;
import com.ideamoment.wing.exception.WingException;
import com.ideamoment.wing.testbean.Bean4Test;

import static org.junit.Assert.*;

/**
 * @author Chinakite Zhang
 *
 */
public class TestClassUtils extends AbstractTestCase {
	@Test
	public void testGetInternalName() {
		String internalName = ClassUtils.getInternalName(Bean4Test.class);
		assertEquals("com/ideamoment/wing/testbean/Bean4Test", internalName);
	}
	
	@Test
	public void testGetTypeDescriptor() {
		assertEquals("Ljava/lang/String;", ClassUtils.getTypeDescriptor(String.class));
		assertEquals("[[Ljava/lang/Object;", ClassUtils.getTypeDescriptor(Object[][].class));
		assertEquals("I", ClassUtils.getTypeDescriptor(Integer.TYPE));
		assertEquals("I", ClassUtils.getTypeDescriptor("int"));
		assertEquals("Z", ClassUtils.getTypeDescriptor("boolean"));
		assertEquals("[I", ClassUtils.getTypeDescriptor("int[]"));
		assertEquals("[[[Ljava/lang/String;", ClassUtils.getTypeDescriptor("java.lang.String[][][]"));
	}
	
	@Test
	public void testGetArrayDimensions() {
		assertEquals(0, ClassUtils.getArrayDimensions("int"));
		assertEquals(1, ClassUtils.getArrayDimensions("int[]"));
		assertEquals(3, ClassUtils.getArrayDimensions("int[][][]"));
	}
	
	@Test(expected=WingException.class)
	public void testGetArrayDimensionsException1() {
		ClassUtils.getArrayDimensions("int[][[]");
	}
	
	@Test(expected=WingException.class)
	public void testGetArrayDimensionsException2() {
		ClassUtils.getArrayDimensions("int]");
	}
	
	@Test(expected=WingException.class)
	public void testGetArrayDimensionsException3() {
		ClassUtils.getArrayDimensions("int[][[]]");
	}
}
