package org.fkjava.cb.service;

import java.util.List;

import org.fkjava.cb.domain.NoticeType;

public interface NoticeService {

	List<NoticeType> findAllTypes();

	void save(NoticeType type);

	void deleteTypeById(String id);

}
