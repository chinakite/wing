/**
 * 
 */
package com.ideamoment.wing.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.ideamoment.wing.AbstractTestCase;
import com.ideamoment.wing.classloader.WingClassLoader;
import com.ideamoment.wing.constant.ModifierConstants;
import com.ideamoment.wing.testbean.Bean4Test;

/**
 * @author Chinakite Zhang
 *
 */
public class TestWingClass extends AbstractTestCase {
	@Test
	public void testAddField() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		wclass.addField(
				ModifierConstants.PUBLIC, 
				String.class, 
				"afield", 
				"test");
		assertEquals(1, wclass.getFields().size());
		assertEquals(ModifierConstants.PUBLIC, wclass.getFields().get("afield").getModifiers());
		assertEquals(String.class, wclass.getFields().get("afield").getType());
		assertEquals("afield", wclass.getFields().get("afield").getName());
		assertEquals("test", wclass.getFields().get("afield").getInitValue());
	}
	
	@Test
	public void testAddMethod() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		WingMethod wmethod = new WingMethod("testMethod", "()V");
		wmethod.setModifiers(ModifierConstants.PUBLIC);
		MethodBody body = new MethodBody() {
			public void body(MethodVisitor methodVisitor) {
				System.out.println("Test Method Body.");
			}
		};
		wclass.addMethod(wmethod, body);
		assertEquals(1, wclass.getMethods().size());
	}
	
	@Test
	public void testAddGetterMethod() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		wclass.addField(
				ModifierConstants.PUBLIC, 
				String.class, 
				"afield", 
				"test")
			  .addGetter("afield");
		assertEquals(1, wclass.getGetterMethods().size());
	}
	
	@Test
	public void testBasicGenerate() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		wclass.setSuperClass(Bean4Test.class);
		wclass.addField(
				ModifierConstants.PUBLIC, 
				String.class, 
				"afield", 
				"test");
		Class testClass = wclass.generate();
		assertEquals("Bean4Test$Wing", testClass.getSimpleName());
		assertEquals(Bean4Test.class, testClass.getSuperclass());
		assertEquals(ModifierConstants.PUBLIC, testClass.getModifiers());
		try {
			assertNotNull(testClass.getField("afield"));
			assertEquals(String.class, testClass.getField("afield").getType());
			assertNotNull(testClass.getMethod("getName"));
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testAddBooleanFieldGenerate() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		wclass.setSuperClass(Bean4Test.class);
		wclass.addField(
				ModifierConstants.PUBLIC, 
				Boolean.TYPE, 
				"afield", 
				true)
			  .addField(
				ModifierConstants.PUBLIC, 
				Boolean.class, 
				"bfield", 
				Boolean.TRUE)
			  .addField(
				ModifierConstants.PUBLIC, 
				Boolean.class, 
				"cfield", 
				false)
				;
		Class testClass = wclass.generate();
		try {
			Object obj = testClass.newInstance();
			assertNotNull(testClass.getField("afield"));
			assertEquals(Boolean.TYPE, testClass.getField("afield").getType());
			assertEquals(true, testClass.getField("afield").getBoolean(obj));
			assertNotNull(testClass.getField("bfield"));
			assertEquals(Boolean.class, testClass.getField("bfield").getType());
			assertEquals(Boolean.TRUE, testClass.getField("bfield").get(obj));
			assertNotNull(testClass.getField("cfield"));
			assertEquals(Boolean.class, testClass.getField("cfield").getType());
			assertEquals(false, testClass.getField("cfield").get(obj));
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (InstantiationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testAddIntegerFieldGenerate() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		wclass.setSuperClass(Bean4Test.class);
		wclass.addField(
				ModifierConstants.PUBLIC, 
				Integer.TYPE, 
				"int1", 
				8)
			  .addField(
				ModifierConstants.PUBLIC, 
				Integer.class, 
				"int2", 
				127)
			  .addField(
				ModifierConstants.PUBLIC, 
				Integer.class, 
				"int3", 
				128)
			  .addField(
				ModifierConstants.PUBLIC, 
				Integer.class, 
				"int4", 
				32767)
			  .addField(
				ModifierConstants.PUBLIC, 
				Integer.class, 
				"int5", 
				32768)
			  .addField(
				ModifierConstants.PUBLIC, 
				Integer.class, 
				"int6", 
				-128)
			  .addField(
				ModifierConstants.PUBLIC, 
				Integer.class, 
				"int7", 
				-129)
			  .addField(
				ModifierConstants.PUBLIC, 
				Integer.class, 
				"int8", 
				-32768)
			  .addField(
				ModifierConstants.PUBLIC, 
				Integer.class, 
				"int9", 
				-32769)
				;
		Class testClass = wclass.generate();
		try {
			Object obj = testClass.newInstance();
			assertNotNull(testClass.getField("int1"));
			assertEquals(Integer.TYPE, testClass.getField("int1").getType());
			assertEquals(8, testClass.getField("int1").getInt(obj));
			assertNotNull(testClass.getField("int2"));
			assertEquals(Integer.class, testClass.getField("int2").getType());
			assertEquals(127, testClass.getField("int2").get(obj));
			assertEquals(128, testClass.getField("int3").get(obj));
			assertEquals(32767, testClass.getField("int4").get(obj));
			assertEquals(32768, testClass.getField("int5").get(obj));
			assertEquals(-128, testClass.getField("int6").get(obj));
			assertEquals(-129, testClass.getField("int7").get(obj));
			assertEquals(-32768, testClass.getField("int8").get(obj));
			assertEquals(-32769, testClass.getField("int9").get(obj));
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (InstantiationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGetterGenerate() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		wclass.setSuperClass(Bean4Test.class);
		wclass.addField(
				ModifierConstants.PUBLIC, 
				String.class, 
				"afield", 
				"test")
			  .addGetter("afield");
		Class testClass = wclass.generate();
		try {
			Method m = testClass.getDeclaredMethod("getAfield");
			assertNotNull(m);
			Object obj = testClass.newInstance();
			Object r = m.invoke(obj);
			assertEquals(String.class, r.getClass());
			assertEquals("test", r);
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail();
		} catch (InstantiationException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testSetterGenerate() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		wclass.setSuperClass(Bean4Test.class);
		wclass.addField(
				ModifierConstants.PUBLIC, 
				String.class, 
				"afield", 
				"test")
			  .addSetter("afield");
		Class testClass = wclass.generate();
		try {
			Method m = testClass.getDeclaredMethod("setAfield", String.class);
			Field f = testClass.getDeclaredField("afield");
			assertNotNull(m);
			Object obj = testClass.newInstance();
			assertEquals("test", f.get(obj));
			m.invoke(obj, "test1");
			assertEquals("test1", f.get(obj));
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail();
		} catch (InstantiationException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMethodInterceptorAdapter() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		wclass.setSuperClass(Bean4Test.class);
		wclass.addField(
				ModifierConstants.PUBLIC, 
				String.class, 
				"afield", 
				"test")
			  .addGetter("afield")
			  .addSetter("afield")
			  .addMethodBeforeInterceptor(new MethodBeforeInterceptor4Test())
			  .addMethodBeforeInterceptor(new MethodBeforeGetterInterceptor4Test())
			  .addMethodAfterInterceptor(new MethodAfterInterceptor4Test())
			  .addMethodAfterInterceptor(new MethodAfterSetterInterceptor4Test());
		Class testClass = wclass.generate();
		try {
			Field f_b1 = testClass.getDeclaredField("before0");
			assertNotNull(f_b1);
			Field f_a1 = testClass.getDeclaredField("after0"); 
			assertNotNull(f_a1);
			Object obj = testClass.newInstance();
			f_b1.setAccessible(true);
			f_a1.setAccessible(true);
			Object before0 = f_b1.get(obj);
			assertNotNull(before0);
			assertEquals(MethodBeforeInterceptor4Test.class, before0.getClass());
			Object after0 = f_a1.get(obj);
			assertNotNull(after0);
			assertEquals(MethodAfterInterceptor4Test.class, after0.getClass());
			Method m_b1 = testClass.getDeclaredMethod("getAfield");
			m_b1.invoke(obj);
			assertEquals(true, Cache4Test.getCache().containsKey("before"));
			assertEquals(true, Cache4Test.getCache().containsKey("after"));
			assertEquals(true, Cache4Test.getCache().containsKey("beforeGetter"));
			assertEquals(false, Cache4Test.getCache().containsKey("afterSetter"));
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail();
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testAddMethodGenerate() {
		WingClass wclass = new WingClass();
		wclass.setClassLoader(new WingClassLoader());
		wclass.setSuperClass(Bean4Test.class);
		WingMethod wmethod = new WingMethod("testMethod", "()V");
		wmethod.setModifiers(ModifierConstants.PUBLIC);
		MethodBody body = new MethodBody() {
			public void body(MethodVisitor methodVisitor) {
				System.out.println("Test Method Body.");
				methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
				methodVisitor.visitLdcInsn("Test Method Body.");
				methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
				methodVisitor.visitInsn(Opcodes.RETURN);
				methodVisitor.visitMaxs(2, 2);
			}
		};
		wclass.addMethod(wmethod, body);
		Class testClass = wclass.generate();
		try {
			assertNotNull(testClass.getMethod("testMethod"));
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail();
		}
	}
}
