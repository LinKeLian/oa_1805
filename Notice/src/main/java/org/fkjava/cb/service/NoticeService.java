package org.fkjava.cb.service;

import java.util.List;

import org.fkjava.cb.domain.Notice;
import org.fkjava.cb.domain.NoticeRead;
import org.fkjava.cb.domain.NoticeType;
import org.springframework.data.domain.Page;

public interface NoticeService {
	/**
	 * 查询所有的类型
	 * @return
	 */
	List<NoticeType> findAllTypes();
	
	void save(NoticeType type);
	/**
	 * 删除类型
	 * @param id
	 */
	void deleteTypeById(String id);

	Page<NoticeRead> findNotices(Integer number, String keyword);

	void write(Notice notice);

	void delete(String id);

	Notice findById(String id);
	/**
	 * 撤回
	 * @param id
	 */
	void recall(String id);
	/**
	 * 发布
	 * @param id
	 */
	void publish(String id);

	void read(String id);
	

}
