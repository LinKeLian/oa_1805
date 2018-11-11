package org.fkjava.identity.service;


import org.fkjava.identity.domain.User;
import org.springframework.data.domain.Page;

public interface IdentityService {

	void save(User user);

	Page<User> findUsers(String keyword, Integer number);

	User findUserById(String id);
	/**
	 * 激活用户
	 * @param id
	 */
	void active(String id);
	/**
	 * 禁用用户
	 * @param id
	 */
	void disable(String id);

	

}
