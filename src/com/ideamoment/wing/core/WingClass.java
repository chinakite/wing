/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.core;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.ideamoment.wing.cache.ClassBytesCache;
import com.ideamoment.wing.classloader.WingClassLoader;
import com.ideamoment.wing.constant.ModifierConstants;
import com.ideamoment.wing.exception.WingException;
import com.ideamoment.wing.util.StringUtils;

/**
 * Wing框架中对类的信息描述类。它是整个框架的核心类。
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public class WingClass {
	
	private Class superClass;		//父类
	
	private Map<String, WingField> fields 
				= new HashMap<String, WingField>();		//字段集合
	private Map<WingMethod, MethodBody> methods 
				= new HashMap<WingMethod, MethodBody>();	//方法集合
	private Map<WingField, WingMethod> getterMethods 
				= new HashMap<WingField, WingMethod>();		//getter方法集合
	private Map<WingField, WingMethod> setterMethods 
				= new HashMap<WingField, WingMethod>();		//setter方法集合

	private Set<MethodBeforeInterceptor> befores 
				= new LinkedHashSet<MethodBeforeInterceptor>();		//方法前回调集合
	private Set<MethodAfterInterceptor> afters 
				= new LinkedHashSet<MethodAfterInterceptor>();		//方法后回调集合
	
	private static Method DEFINE_CLASS;
    private static final ProtectionDomain PROTECTION_DOMAIN;
    private ClassLoader classLoader;

	static {
        PROTECTION_DOMAIN = (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return WingClass.class.getProtectionDomain();
            }
        });
        
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Class loader = Class.forName("java.lang.ClassLoader"); // JVM crash w/o this
                    DEFINE_CLASS = loader.getDeclaredMethod("defineClass",
                                                            new Class[]{ String.class,
                                                                         byte[].class,
                                                                         Integer.TYPE,
                                                                         Integer.TYPE,
                                                                         ProtectionDomain.class });
                    DEFINE_CLASS.setAccessible(true);
                } catch (ClassNotFoundException e) {
                    throw new WingException(e);
                } catch (NoSuchMethodException e) {
                    throw new WingException(e);
                }
                return null;
            }
        });
    }
	
	/**
	 * 给WingClass添加一个字段。
	 * 
	 * @param modifiers 修饰符
	 * @param type 字段类型
	 * @param name 字段名称
	 * @param initValue 初始值
	 * @return 当前WingClass实例
	 */
	public WingClass addField(int modifiers, Class type, String name, Object initValue) {
		WingField field = new WingField(modifiers, type, name, initValue);
		this.fields.put(name, field);
		return this;
	}
	
	/**
	 * 给WingClass添加一个字段。
	 * 
	 * @param field 一个WingField类型的字段描述
	 * @return 当前WingClass实例
	 */
	public WingClass addField(WingField field) {
		this.fields.put(field.getName(), field);
		return this;
	}
	
	/**
	 * 给WingClass添加一个方法。
	 * 
	 * @param method 一个WingMethod类型的方法描述
	 * @param body 方法体
	 * @return 当前WingClass实例
	 */
	public WingClass addMethod(WingMethod method, MethodBody body) {
		methods.put(method, body);
		return this;
	}
	
	/**
	 * 为类的一个字段添加一个Getter方法，
	 * 如果该字段不存在或是已存在getter方法声明则抛出WingException异常
	 * 
	 * @param field 要添加Getter方法的字段
	 * @return 当前WingClass的实例
	 */
	public WingClass addGetter(WingField field) {
		//验证是否可以添加Getter方法，如不可添加则抛出WingException异常。
		validateGetter(field.getName());
		
		WingMethod wMethod = new WingMethod();
		wMethod.setModifiers(ModifierConstants.PUBLIC);
		wMethod.setReturnType(Type.getType(field.getType()));
		wMethod.setName("get" + StringUtils.toTitleCase(field.getName()));
		getterMethods.put(field, wMethod);
		
		return this;
	}

	public WingClass addGetter(String fieldName) {
		//验证是否可以添加Getter方法，如不可添加则抛出WingException异常。
		validateGetter(fieldName);
		
		WingMethod wMethod = new WingMethod();
		wMethod.setModifiers(ModifierConstants.PUBLIC);
		wMethod.setReturnType(Type.getType(fields.get(fieldName).getType()));
		wMethod.setName("get" + StringUtils.toTitleCase(fieldName));
		getterMethods.put(fields.get(fieldName), wMethod);
		
		return this;
	}
	
	public WingClass addSetter(WingField field) {
		//验证是否可以添加Setter方法，如不可添加则抛出WingException异常。
		validateSetter(field.getName());
		
		WingMethod wMethod = new WingMethod();
		wMethod.setModifiers(ModifierConstants.PUBLIC);
		wMethod.setReturnType(Type.VOID_TYPE);
		wMethod.setArgTypes(new Type[]{Type.getType(field.getType())});
		wMethod.setName("set" + StringUtils.toTitleCase(field.getName()));
		
		setterMethods.put(field, wMethod);
		
		return this;
	}
	
	public WingClass addSetter(String fieldName) {
		//验证是否可以添加Setter方法，如不可添加则抛出WingException异常。
		validateSetter(fieldName);
		
		WingMethod wMethod = new WingMethod();
		wMethod.setModifiers(ModifierConstants.PUBLIC);
		wMethod.setReturnType(Type.VOID_TYPE);
		wMethod.setArgTypes(new Type[]{Type.getType(fields.get(fieldName).getType())});
		wMethod.setName("set" + StringUtils.toTitleCase(fieldName));
		
		setterMethods.put(fields.get(fieldName), wMethod);
		
		return this;
	}
	
	public WingClass addMethodBeforeInterceptor(MethodBeforeInterceptor interceptor) {
		befores.add(interceptor);
		return this;
	}
	
	public WingClass addMethodAfterInterceptor(MethodAfterInterceptor interceptor) {
		afters.add(interceptor);
		return this;
	}
	
	/**
	 * 根据准备好的信息生成字节码的类。
	 */
	public Class generate() {
		String targetClassName = superClass.getName() + "$Wing";
		if(ClassBytesCache.getClass(targetClassName) != null) {
			return ClassBytesCache.getClass(targetClassName);
		}else{
			ClassWriter writer = new ClassWriter(0);
			writer.visit(Opcodes.V1_5, 
					     Opcodes.ACC_PUBLIC, 
					     Type.getInternalName(superClass) + "$Wing", 
					     null,
					     Type.getInternalName(superClass),
						 null);
			
			//生成所有字段
			for(String fieldName : fields.keySet()) {
				writer.visitField(fields.get(fieldName).getModifiers(), 
								  fields.get(fieldName).getName(), 
								  Type.getType(fields.get(fieldName).getType()).getDescriptor(),
						          null, 
						          fields.get(fieldName).getInitValue())
					  .visitEnd();
			}
			
			//生成所有before拦截器实例
			int i=0;
			for(MethodBeforeInterceptor before : befores) {
				writer.visitField(Opcodes.ACC_PRIVATE, "before"+i, "Lcom/ideamoment/wing/core/MethodBeforeInterceptor;", null, null).visitEnd();
				i++;
			}
			
			//生成所有after拦截器实例
			i=0;
			for(MethodAfterInterceptor after : afters) {
				writer.visitField(Opcodes.ACC_PRIVATE, "after"+i, "Lcom/ideamoment/wing/core/MethodAfterInterceptor;", null, null).visitEnd();
				i++;
			}
			
			//生成无参构造函数
			MethodVisitor mv = writer.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
	        mv.visitVarInsn(Opcodes.ALOAD, 0);
	        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(superClass), "<init>", "()V");
	       
	        //在构造函数中设置字段的初始值
	        for(String fieldName : fields.keySet()) {
				WingField field = fields.get(fieldName);
				mv.visitVarInsn(Opcodes.ALOAD, 0);

				if(field.getType().equals(String.class)) {
					mv.visitLdcInsn(field.getInitValue());
				}else if(field.getType().equals(Boolean.class)) {
					if((Boolean)field.getInitValue()){
						mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
					}else{
						mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
					}
				}else if(field.getType().equals(Boolean.TYPE)) {
					if((Boolean)field.getInitValue()){
						mv.visitInsn(Opcodes.ICONST_1);
					}else{
						mv.visitInsn(Opcodes.ICONST_0);
					}
				}else if(field.getType().equals(Integer.TYPE)) {
					int initValue = (Integer)field.getInitValue();
					if(initValue == -1) {
						mv.visitInsn(Opcodes.ICONST_M1);
					} else if(initValue == 0) {
						mv.visitInsn(Opcodes.ICONST_0);
					} else if(initValue == 1) {
						mv.visitInsn(Opcodes.ICONST_1);
					} else if(initValue == 2) {
						mv.visitInsn(Opcodes.ICONST_2);
					} else if(initValue == 3) {
						mv.visitInsn(Opcodes.ICONST_3);
					} else if(initValue == 4) {
						mv.visitInsn(Opcodes.ICONST_4);
					} else if(initValue == 5) {
						mv.visitInsn(Opcodes.ICONST_5);
					} else if(initValue > 5 && initValue <= 127) {
						mv.visitIntInsn(Opcodes.BIPUSH, initValue);
					} else if(initValue >= -128 && initValue < -1) {
						mv.visitIntInsn(Opcodes.BIPUSH, initValue);
					} else if(initValue >= 128 && initValue <= 32767) {
						mv.visitIntInsn(Opcodes.SIPUSH, initValue);
					} else if(initValue < -128 && initValue >= -32768) {
						mv.visitIntInsn(Opcodes.SIPUSH, initValue);
					} else {
						mv.visitLdcInsn(new Integer(initValue));
					}
				}else if(field.getType().equals(Integer.class)) {
					mv.visitTypeInsn(Opcodes.NEW, "java/lang/Integer");
					mv.visitInsn(Opcodes.DUP);
					int initValue = (Integer)field.getInitValue();
					if(initValue == -1) {
						mv.visitInsn(Opcodes.ICONST_M1);
					} else if(initValue == 0) {
						mv.visitInsn(Opcodes.ICONST_0);
					} else if(initValue == 1) {
						mv.visitInsn(Opcodes.ICONST_1);
					} else if(initValue == 2) {
						mv.visitInsn(Opcodes.ICONST_2);
					} else if(initValue == 3) {
						mv.visitInsn(Opcodes.ICONST_3);
					} else if(initValue == 4) {
						mv.visitInsn(Opcodes.ICONST_4);
					} else if(initValue == 5) {
						mv.visitInsn(Opcodes.ICONST_5);
					} else if(initValue > 5 && initValue <= 127) {
						mv.visitIntInsn(Opcodes.BIPUSH, initValue);
					} else if(initValue >= -128 && initValue < -1) {
						mv.visitIntInsn(Opcodes.BIPUSH, initValue);
					} else if(initValue >= 128 && initValue <= 32767) {
						mv.visitIntInsn(Opcodes.SIPUSH, initValue);
					} else if(initValue < -128 && initValue >= -32768) {
						mv.visitIntInsn(Opcodes.SIPUSH, initValue);
					} else {
						mv.visitLdcInsn(new Integer(initValue));
					}
					mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Integer", "<init>", "(I)V");
				}
		        mv.visitFieldInsn(Opcodes.PUTFIELD, Type.getInternalName(superClass) + "$Wing", field.getName(), Type.getType(field.getType()).getDescriptor());
			}
	        
	        //初始化所有before拦截器实例
	        i=0;
	        for(MethodBeforeInterceptor before : befores) {
				mv.visitVarInsn(Opcodes.ALOAD, 0);
				mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(before.getClass()));
				mv.visitInsn(Opcodes.DUP);
				mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(before.getClass()), "<init>", "()V");
				mv.visitFieldInsn(Opcodes.PUTFIELD, Type.getInternalName(superClass) + "$Wing", "before" + i, "Lcom/ideamoment/wing/core/MethodBeforeInterceptor;");
				i++;
			}
	        
	        //初始化所有after拦截器实例
	        i=0;
	        for(MethodAfterInterceptor after : afters) {
				mv.visitVarInsn(Opcodes.ALOAD, 0);
				mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(after.getClass()));
				mv.visitInsn(Opcodes.DUP);
				mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(after.getClass()), "<init>", "()V");
				mv.visitFieldInsn(Opcodes.PUTFIELD, Type.getInternalName(superClass) + "$Wing", "after" + i, "Lcom/ideamoment/wing/core/MethodAfterInterceptor;");
				i++;
			}
	        
	        mv.visitInsn(Opcodes.RETURN);
	        mv.visitMaxs(4, 1);
	        mv.visitEnd();
			
			//生成Getter方法
			for(WingField field : getterMethods.keySet()) {
				WingMethod wMethod = getterMethods.get(field);
				MethodVisitor methodVisitor = writer.visitMethod(
						wMethod.getModifiers(), 
						wMethod.getName(), 
						Type.getMethodDescriptor(wMethod.getReturnType(), new Type[]{}), 
						null, 
						null);
				methodVisitor.visitCode();
				
				i=0;
		        for(MethodBeforeInterceptor before : befores) {
		        	if(before.adapt(wMethod.getName())) {
			        	methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
			        	methodVisitor.visitFieldInsn(
			        			Opcodes.GETFIELD, 
			        			Type.getInternalName(superClass) + "$Wing",
			        			"before" + i, 
			        			Type.getDescriptor(MethodBeforeInterceptor.class));
			        	methodVisitor.visitMethodInsn(
			        			Opcodes.INVOKEINTERFACE, 
			        			Type.getInternalName(MethodBeforeInterceptor.class), 
			        			"before", 
			        			"()V");
		        	}
		        	i++;
		        }
		        
		        i=0;
		        for(MethodAfterInterceptor after : afters) {
		        	if(after.adapt(wMethod.getName())) {
			        	methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
			        	methodVisitor.visitFieldInsn(
			        			Opcodes.GETFIELD, 
			        			Type.getInternalName(superClass) + "$Wing",
			        			"after" + i, 
			        			Type.getDescriptor(MethodAfterInterceptor.class));
			        	methodVisitor.visitMethodInsn(
			        			Opcodes.INVOKEINTERFACE, 
			        			Type.getInternalName(MethodAfterInterceptor.class), 
			        			"after", 
			        			"()V");
		        	}
		        	i++;
		        }
				
				methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
				methodVisitor.visitFieldInsn(
						Opcodes.GETFIELD, 
						Type.getInternalName(superClass) + "$Wing", 
						field.getName(), 
						Type.getType(field.getType()).getDescriptor());
				methodVisitor.visitInsn(getReturnOpcode(wMethod.getReturnType()));
				methodVisitor.visitMaxs(1, 1);
				methodVisitor.visitEnd();
			}
			
			//生成Setter方法
			for(WingField field : setterMethods.keySet()) {
				WingMethod wMethod = setterMethods.get(field);
				MethodVisitor methodVisitor = writer.visitMethod(
						wMethod.getModifiers(), 
						wMethod.getName(), 
						Type.getMethodDescriptor(wMethod.getReturnType(), wMethod.getArgTypes()), 
						null, 
						null);
				methodVisitor.visitCode();
				methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
				methodVisitor.visitVarInsn(Opcodes.ALOAD, 1);
				methodVisitor.visitFieldInsn(
						Opcodes.PUTFIELD, 
						Type.getInternalName(superClass) + "$Wing", 
						field.getName(), 
						Type.getType(field.getType()).getDescriptor());
				methodVisitor.visitInsn(Opcodes.RETURN);
				methodVisitor.visitMaxs(2, 2);
				methodVisitor.visitEnd();
			}
			
			//生成Method
			for(WingMethod method : methods.keySet()) {
				MethodVisitor methodVisitor = writer.visitMethod(
						method.getModifiers(), 
						method.getName(), 
						method.getMethodDescriptor(), 
						null, 
						null);
				methodVisitor.visitCode();
				MethodBody body = methods.get(method);
				body.body(methodVisitor);
				methodVisitor.visitEnd();
			}
			
			writer.visitEnd();
			byte[] classBytes = writer.toByteArray();
			
//			WingClassLoader cl = new WingClassLoader();
//			Class clazz = cl.defineClass(targetClassName, classBytes);
			
			ClassLoader loader = getClassLoader();
			Class clazz;
			try {
				clazz = defineClass(targetClassName, classBytes, loader);
				ClassBytesCache.put(targetClassName, clazz);
				return clazz;
			} catch (Exception e) {
				e.printStackTrace();
				throw new WingException(e);
			}
		}
	}

	/**
	 * 验证是否可以添加Getter方法，如不可添加则抛出WingException异常。
	 * 
	 * @param fieldName 要添加getter方法的字段名称。
	 */
	private void validateGetter(String fieldName) {
		if(!fields.containsKey(fieldName)) {
			throw new WingException("The field [" + fieldName + "] is not found.");
		}
		if(getterMethods.containsKey(fields.get(fieldName))) {
			throw new WingException("The getter method of field [" + fieldName + "] is already exist.");
		}
	}
	
	/**
	 * 验证是否可以添加Setter方法，如不可添加则抛出WingException异常。
	 * 
	 * @param fieldName 要添加setter方法的字段名称。
	 */
	private void validateSetter(String fieldName) {
		if(!fields.containsKey(fieldName)) {
			throw new WingException("The field [" + fieldName + "] is not found.");
		}
		if(setterMethods.containsKey(fields.get(fieldName))) {
			throw new WingException("The setter method of field [" + fieldName + "] is already exist.");
		}
	}
	
	private int getReturnOpcode(Type returnType) {
		if(returnType.equals(Type.BOOLEAN_TYPE) 
				|| returnType.equals(Type.BYTE_TYPE) 
				|| returnType.equals(Type.CHAR_TYPE)
				|| returnType.equals(Type.DOUBLE_TYPE)
				|| returnType.equals(Type.FLOAT_TYPE)
				|| returnType.equals(Type.INT_TYPE)
				|| returnType.equals(Type.LONG_TYPE)
				|| returnType.equals(Type.SHORT_TYPE)
				) {
			return returnType.getOpcode(Opcodes.IRETURN);
		}else if(returnType.equals(Type.VOID_TYPE)) {
			return Opcodes.RETURN;
		}else{
			return Opcodes.ARETURN;
		}
	}
	
	public static Class defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
        Object[] args = new Object[]{className, b, new Integer(0), new Integer(b.length), PROTECTION_DOMAIN };
        return (Class)DEFINE_CLASS.invoke(loader, args);
    }
	
	public ClassLoader getClassLoader() {
        ClassLoader t = classLoader;
        if (t == null) {
            t = getDefaultClassLoader();
        }
        if (t == null) {
            t = getClass().getClassLoader();
        }
        if (t == null) {
            t = Thread.currentThread().getContextClassLoader();
        }
        if (t == null) {
            throw new IllegalStateException("Cannot determine classloader");
        }
        return t;
    }
	
	private ClassLoader getDefaultClassLoader() {
		return null;
	}
	
	//------------------------------------------------------------------------------
	// Getter & Setter
	//------------------------------------------------------------------------------
	public Class getSuperClass() {
		return superClass;
	}

	public void setSuperClass(Class superClass) {
		this.superClass = superClass;
	}
	
	public Map<String, WingField> getFields() {
		return fields;
	}

	public void setFields(Map<String, WingField> fields) {
		this.fields = fields;
	}

	public Map<WingMethod, MethodBody> getMethods() {
		return methods;
	}

	public void setMethods(Map<WingMethod, MethodBody> methods) {
		this.methods = methods;
	}

	public Map<WingField, WingMethod> getGetterMethods() {
		return getterMethods;
	}

	public void setGetterMethods(Map<WingField, WingMethod> getterMethods) {
		this.getterMethods = getterMethods;
	}

	public Map<WingField, WingMethod> getSetterMethods() {
		return setterMethods;
	}

	public void setSetterMethods(Map<WingField, WingMethod> setterMethods) {
		this.setterMethods = setterMethods;
	}

	public Set<MethodBeforeInterceptor> getBefores() {
		return befores;
	}

	public void setBefores(Set<MethodBeforeInterceptor> befores) {
		this.befores = befores;
	}

	public Set<MethodAfterInterceptor> getAfters() {
		return afters;
	}

	public void setAfters(Set<MethodAfterInterceptor> afters) {
		this.afters = afters;
	}
	
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
}
