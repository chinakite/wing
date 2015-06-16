/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.core;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;

/**
 * Wing框架中方法的信息描述类。
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public class WingMethod {
	private String name;	//方法名
	private String methodDescriptor;	//方法描述
	private int modifiers;		//修饰符
	private Type[] argTypes = new Type[]{};		//参数类型
	private Type returnType;	//返回类型
	private List<Exception> exceptions = new ArrayList<Exception>();	//方法的异常表
	
	public WingMethod(String name, String methodDescriptor) {
		this.name = name;
		this.methodDescriptor = methodDescriptor;
	}
	
	public WingMethod() {
	}
	
	//-------------------------------------------------
	// Getter & Setter
	//-------------------------------------------------
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMethodDescriptor() {
		return methodDescriptor;
	}
	public void setMethodDescriptor(String methodDescriptor) {
		this.methodDescriptor = methodDescriptor;
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public List<Exception> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<Exception> exceptions) {
		this.exceptions = exceptions;
	}
	
	public boolean addException(Exception e) {
		return this.addException(e);
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public Type[] getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(Type[] argTypes) {
		this.argTypes = argTypes;
	}
}
