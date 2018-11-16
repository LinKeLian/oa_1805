package org.fkjava.cb.dao;

import org.fkjava.cb.domain.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeDao extends JpaRepository<NoticeType, String>{

	NoticeType findByName(String name);

}
