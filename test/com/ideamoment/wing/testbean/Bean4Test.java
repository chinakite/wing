/**
 * 
 */
package com.ideamoment.wing.testbean;

import com.ideamoment.wing.core.MethodAfterInterceptor;
import com.ideamoment.wing.core.MethodAfterInterceptor4Test;
import com.ideamoment.wing.core.MethodBeforeInterceptor;
import com.ideamoment.wing.core.MethodBeforeInterceptor4Test;

/**
 * @author Chinakite Zhang
 *
 */
public class Bean4Test {
	private String id;
	private String name;
	private int age;
	
	private MethodBeforeInterceptor before1 = new MethodBeforeInterceptor4Test();
	private MethodBeforeInterceptor before2 = new MethodBeforeInterceptor4Test();
	private MethodBeforeInterceptor before3 = new MethodBeforeInterceptor4Test();
	
	private MethodAfterInterceptor after1 = new MethodAfterInterceptor4Test();
	private MethodAfterInterceptor after2 = new MethodAfterInterceptor4Test();
	
	public Bean4Test(){
		name = "test";
		id = "testtest";
	}
	
	public String getId() {
		before1.before();
		before2.before();
		after1.after();
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
