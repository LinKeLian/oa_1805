package org.fkjava.menu.dao;

import java.util.List;

import org.fkjava.identity.domain.Role;
import org.fkjava.menu.domain.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface MenuDao extends JpaRepository<Menu, String>{

	Menu findByNameAndParent(String name, Menu parent);

	Menu findByNameAndParentNull(String name);
	@Query("select max(number) from Menu where parent is null")
	Double findMaxNumberByParentNull();
	//使用冒号是命名参数，通过参数名来传值
	// 使用使用?1表示非命名参数，通过第几个参数类传值
	@Query("select max(number) from Menu where parent = :parent")
	Double findMaxNumberByParent(@Param("parent")Menu parent);
	//查询一级目录
	List<Menu> findByParentNullOrderByNumber();
	
	/**
	 * 查询parent为指定参数的菜单，并且number要小于参数。返回的结果以number降序排列
	 * 
	 * @param parent   上级菜单
	 * @param number   要找小于number的菜单
	 * @param pageable 只要查询1条记录
	 * @return
	 */
	Page<Menu> findByParentAndNumberLessThanOrderByNumberDesc(Menu parent, Double number, Pageable pageable);

	/**
	 * 查询parent为指定参数的菜单，并且number要大于参数。返回的结果以number升序排列
	 * 
	 * @param parent
	 * @param number
	 * @param pageable
	 * @return
	 */
	Page<Menu> findByParentAndNumberGreaterThanOrderByNumberAsc(Menu parent, Double number, Pageable pageable);

	//Distinct有多个角色可以去重（菜单)
	List<Menu> findDistinctByRolesIn(List<Role> roles);

}
