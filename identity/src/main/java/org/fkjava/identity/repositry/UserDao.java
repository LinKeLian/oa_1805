package org.fkjava.identity.repositry;

import org.fkjava.identity.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String>{

	User findByLoginName(String loginName);
	//等于查询
	Page<User> findByName(String keyword, Pageable pageable);
	
	//前后模糊查询
	Page<User> findByNameContaining(String keyword, Pageable pageable);
	

}
