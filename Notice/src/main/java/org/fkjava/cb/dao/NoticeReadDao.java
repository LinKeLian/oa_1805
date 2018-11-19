package org.fkjava.cb.dao;

import java.util.Date;

import org.fkjava.cb.domain.Notice;
import org.fkjava.cb.domain.NoticeRead;
import org.fkjava.identity.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface NoticeReadDao extends JpaRepository<NoticeRead, String>{
	
	/**
	 * 
	 * 
	 * left outer join以左边的表为主
	 * 
	 * 排序  状态为DRAFT即草稿有0为升序
	 * 撤回在最下面
	 * 发布为降序，后发布在上面
	 * order by case n.status when 'DRAFT' then 0 
	 * when 'RECALL' then 99 
	 * when 'RELEASED' then 1 end asc, n.releaseTime desc 
	 * @param author
	 * @param user
	 * @param pageable
	 * @return
	 */
	@Query("select new NoticeRead(nr.id, nr.readTime, n)"//
			+ " from Notice n "//
			+ " left outer join NoticeRead nr on nr.notice = n and nr.user = :user "
			+ " where (n.author = :author and (n.status = 'DRAFT' or n.status = 'RECALL') "//
			+ " or n.status = 'RELEASED')" //
			+ " order by case n.status when 'DRAFT' then 0 when 'RECALL' then 99 when 'RELEASED' then 1 end asc, n.releaseTime desc")
	Page<NoticeRead> findNotices(@Param("author") User author, @Param("user") User user, Pageable pageable);

	NoticeRead findByNoticeAndUser(Notice notice, User user);

			

	


}
