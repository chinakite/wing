/***
 * Wing : a small and powerful Java class enhance framework.
 * Copyright (c) 2010-2014 IdeaMoment Team, China Beijing
 * All rights reserved.
 * 
 */
package com.ideamoment.wing.core;

import org.objectweb.asm.Type;

/**
 * Wing框架中字段的信息描述类。
 * 
 * @author Chinakite Zhang
 * @version 20100915
 * @since 0.1
 */
public class WingField {
	
	private int modifiers;
	private Class type;
	private String name;
	private Object initValue;

	public WingField(
				int modifiers,
				Class type,
				String name,
				Object initValue) {
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
		this.initValue = initValue;
	}
	
	//---------------------------------------------------
	// Getter & Setter
	//---------------------------------------------------
	
	public int getModifiers() {
		return modifiers;
	}
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getInitValue() {
		return initValue;
	}
	public void setInitValue(Object initValue) {
		this.initValue = initValue;
	}
}
