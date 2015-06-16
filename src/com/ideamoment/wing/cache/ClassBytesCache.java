/**
 * 
 */
package com.ideamoment.wing.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class字节码的缓存
 * 
 * @author Chinakite Zhang
 * @version 2010-11-19
 * @since 0.1
 */
public class ClassBytesCache implements Serializable {

	private static final long serialVersionUID = 1300914278852635836L;
	
	/**
	 * 缓存，使用ConcurrentHashMap提高并发能力。
	 */
	private static Map<String, Class> cache = new ConcurrentHashMap<String, Class>();
	
	/**
	 * 私有构造函数，单例模式
	 */
	private ClassBytesCache() {
	}
	
	/**
	 * 根据指定的键值（一般是类的全名）获取类的字节码
	 * 
	 * @param key 缓存中的键值
	 * @return 字节码
	 */
	public static Class getClass(String key) {
		return cache.get(key);
	}
	
	/**
	 * 缓存一个类的字节码信息
	 * 
	 * @param key 键值（一般是类的全名）
	 * @param bytes 字节码
	 */
	public static void put(String key, Class bytes) {
		cache.put(key, bytes);
	}
	
	/**
	 * 清空缓存
	 */
	public static void clear() {
		cache.clear();
	}
	
	/**
	 * 删除一个缓存对象
	 * 
	 * @param key 键值
	 */
	public static void removeClass(String key) {
		cache.remove(key);
	}
}
