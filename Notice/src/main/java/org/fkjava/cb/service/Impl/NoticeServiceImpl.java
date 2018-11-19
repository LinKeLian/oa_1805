package org.fkjava.cb.service.Impl;

import java.util.Date;
import java.util.List;

import org.fkjava.cb.dao.NoticeDao;
import org.fkjava.cb.dao.NoticeReadDao;
import org.fkjava.cb.dao.NoticeTypeDao;
import org.fkjava.cb.domain.Notice;
import org.fkjava.cb.domain.Notice.Status;
import org.fkjava.cb.domain.NoticeRead;
import org.fkjava.cb.domain.NoticeType;
import org.fkjava.cb.service.NoticeService;
import org.fkjava.identity.UserHolder;
import org.fkjava.identity.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
@Service
public class NoticeServiceImpl implements NoticeService {
 	@Autowired
	private NoticeTypeDao noticeTypeDao;
 	
 	@Autowired
	private NoticeDao noticeDao;
 	@Autowired
	private NoticeReadDao noticeReadDao;
 	
 	@Override
	public List<NoticeType> findAllTypes() {
		Sort sort = Sort.by("name");
 		return this.noticeTypeDao.findAll(sort);
	}
 	@Override
	public void save(NoticeType type) {
		NoticeType old = this.noticeTypeDao.findByName(type.getName());
		if (old == null || old.getId().equals(type.getId())) {
			// 要么新的、要么修改的
			this.noticeTypeDao.save(type);
		} else {
			throw new IllegalArgumentException("公告类型的名称不能重复！");
		}
	}
 	@Override
	public void deleteTypeById(String id) {
		this.noticeTypeDao.deleteById(id);
	}
 	@Override
	public Page<NoticeRead> findNotices(Integer number, String keyword) {
		// 标题、撰写时间、作者都可以非常方便查询出来，内容在列表显示的时候不关心
		// 状态：草稿、已发布、已撤回
		// 还有一个特殊状态：不同的用户有阅读状态，没有阅读、并且已经发布的，使用粗体字显示
		// 还未发布的，只能修改；已撤回、已发布不能再修改。

		// 要查询的列表数据包括：
		// 1.当前用户自己写的、还未发布的
		// 2.已经发布、可以阅读，需要查询阅读状态，表关联查询
		// 3.已经撤回的，只有作者能够查看
		User author = UserHolder.get();
		Pageable pageable = PageRequest.of(number, 10);
		// Page<Notice> page = this.noticeRepository.findNotices(author, pageable);

		// 如果没有阅读记录，那么也会有公告记录
		Page<NoticeRead> dataPage = this.noticeReadDao.findNotices(author, author, pageable);
		List<NoticeRead> content = dataPage.getContent();

		Page<NoticeRead> page = new PageImpl<>(content, pageable, dataPage.getTotalElements());

		return page;
	}
 	
 	@Override
	public void write(Notice notice) {
		// 1.填充字段
		notice.setAuthor(UserHolder.get());
		notice.setWriteTime(new Date());
		notice.setReleaseTime(null);
		notice.setStatus(Status.DRAFT);

		if (StringUtils.isEmpty(notice.getId())) {
			notice.setId(null);
		}

		// 2.保存公告
		this.noticeDao.save(notice);
	}
 	//跟据id查询内容
 	@Override
 	public Notice findById(String id) {
 		
 		return this.noticeDao.findById(id).orElse(null);
 	}
 	//删除草稿
	@Override
	public void delete(String id) {
		Notice notice = this.findById(id);
		if(notice != null) {
			this.noticeDao.delete(notice);
		}
	}
	@Override
	@Transactional
	public void recall(String id) {
		Notice notice = this.findById(id);
		if(notice != null) {
			notice.setStatus(Status.RECALL);
		}
	}
	@Override
	@Transactional
	public void publish(String id) {
		Notice notice = this.findById(id);
		if(notice != null) {
			notice.setStatus(Status.RELEASED);
			notice.setReleaseTime(new Date());
		}
	}
	@Override
	@Transactional
	public void read(String id) {
		//当前线程
		User user = UserHolder.get();
		//公告的id知道那一片
		Notice notice = this.findById(id);
		//阅读时间
		Date readTime = new Date();

		NoticeRead old = this.noticeReadDao.findByNoticeAndUser(notice, user);
		if (old == null) {
			NoticeRead nr = new NoticeRead();
			nr.setNotice(notice);
			nr.setReadTime(readTime);
			nr.setUser(user);

			this.noticeReadDao.save(nr);
		}
	}
	
}