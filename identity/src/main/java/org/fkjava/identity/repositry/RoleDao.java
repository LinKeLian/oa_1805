package org.fkjava.identity.repositry;


import java.util.List;
import java.util.Optional;

import org.fkjava.identity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RoleDao extends JpaRepository<Role, String>{
	
	//Optional表示可要可不要
	Optional<Role> findByRoleKey(String roleKey);

	List<Role> findByFixedTrue();

	List<Role> findByFixedFalseOrderByName();

	

}
