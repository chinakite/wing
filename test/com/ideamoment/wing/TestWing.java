/**
 * 
 */
package com.ideamoment.wing;

import org.junit.Test;
import org.objectweb.asm.Opcodes;

import com.ideamoment.wing.core.WingClass;
import com.ideamoment.wing.testbean.Bean4Test;

import static org.junit.Assert.*;

/**
 * Wing的入口程序测试类。
 * 
 * @author Chinakite Zhang
 *
 */
public class TestWing extends AbstractTestCase {
	@Test
	public void testCreateClass() {
		WingClass wclass = Wing.createSubClass(Bean4Test.class);
		wclass.addField(Opcodes.ACC_PUBLIC, String.class, "afield", "abc");
		Class testClass = wclass.generate();
		assertEquals("Bean4Test$Wing", testClass.getSimpleName());
		assertEquals(testClass.getSuperclass(), Bean4Test.class);
	}
}
