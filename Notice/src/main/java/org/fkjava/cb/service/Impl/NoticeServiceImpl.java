package org.fkjava.cb.service.Impl;

import java.util.List;

import org.fkjava.cb.dao.NoticeDao;
import org.fkjava.cb.domain.NoticeType;
import org.fkjava.cb.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
@Service
public class NoticeServiceImpl implements NoticeService {
 	@Autowired
	private NoticeDao noticeDao;
 	@Override
	public List<NoticeType> findAllTypes() {
		Sort sort = Sort.by("name");
 		return this.noticeDao.findAll(sort);
	}
 	@Override
	public void save(NoticeType type) {
		NoticeType old = this.noticeDao.findByName(type.getName());
		if (old == null || old.getId().equals(type.getId())) {
			// 要么新的、要么修改的
			this.noticeDao.save(type);
		} else {
			throw new IllegalArgumentException("公告类型的名称不能重复！");
		}
	}
 	@Override
	public void deleteTypeById(String id) {
		this.noticeDao.deleteById(id);
	}
}