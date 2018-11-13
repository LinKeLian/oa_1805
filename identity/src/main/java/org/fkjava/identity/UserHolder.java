package org.fkjava.identity;

import org.fkjava.identity.domain.User;

/**
 * 此工具的目的是把User存储在当前线程里面，方便在其他的模块使用
 * @author Administrator
 *
 */
public class UserHolder {
	
	private static final ThreadLocal<User> THREAD_LOCAL = new ThreadLocal<>();
	
	public static User get() {
		return THREAD_LOCAL.get();
	}
	
	public static void set(User user) {
		THREAD_LOCAL.set(user);
	}
	
	public static void remove() {
		THREAD_LOCAL.remove();
	}
	
	
	
}
