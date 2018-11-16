package org.fkjava.storage.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

import org.fkjava.common.data.domain.Result;
import org.fkjava.identity.UserHolder;
import org.fkjava.identity.domain.User;
import org.fkjava.storage.dao.StorageDao;
import org.fkjava.storage.domain.FileInfo;
import org.fkjava.storage.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StorageServiceImpl implements StorageService{
	//打印日志文件
	private static final Logger LOG = LoggerFactory.getLogger(StorageServiceImpl.class);
	
	// 使用普通Spring项目的时候，通过PropertyPlaceHolder方法加载文件的时候，可以利用@Value注入属性的值
	// 冒号后面不能有空格，最好前面也不要有
	// 冒号后面表示【默认值】
	//@Value(value="${fkjava.storage.dir?:/tem/storage}")
	private String dir = "/tem/storage";
	public void setDir(String dir) {
		this.dir = dir;
	}
	@Autowired
	private StorageDao  storageDao;
	
	
	@Override
	public void save(FileInfo info, InputStream in) {
		//保存文件
		String path = UUID.randomUUID().toString();//保存的文件名
		//System.out.println(path);
		File file = new File(dir,path);
		//没有创建一个文件夹
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		LOG.trace("文件的实际存储路径:{}",file.getAbsolutePath());
		Path target = file.toPath();//路径加保存的uuid
		//System.out.println(target);
		try {
			Files.copy(in, target);
		}catch (Exception e) {
			throw new RuntimeException("保存到硬盘失败：" + e.getMessage(),e);
		}
		
		//保存文件信息
		info.setOwner(UserHolder.get());
		info.setUploadTime(new Date());
		info.setPath(path);
		
		this.storageDao.save(info);
	}
	
	/**
	 * 查询
	 */
	@Override
	public Page<FileInfo> findFiles(String keyword, Integer number) {
		//查询当前用户的信息
		User user = UserHolder.get();
		if(StringUtils.isEmpty(keyword)) {
			keyword = null;
		}
		Pageable pageable = PageRequest.of(number, 5);
		Page<FileInfo> page;
		if(keyword == null) {
			page = this.storageDao.findByOwner(user,pageable);
		}else {
			page = this.storageDao.findByOwnerAndNameContaining(user,keyword,pageable);
		}
		
		return page;
	}

	@Override
	public FileInfo findById(String id) {
		return this.storageDao.findById(id).orElse(null);
	}

	@Override
	public InputStream getFileContent(FileInfo fi) throws FileNotFoundException {
		try {
			File file = new File(dir,fi.getPath());
			System.out.println(dir+"       " +fi.getPath());
			FileInputStream inputStream = new FileInputStream(file);
			return inputStream;
		}catch (FileNotFoundException e) {
			LOG.trace("文件没有找到：" + e.getLocalizedMessage(), e);
			return null;
		}
	}

	@Override
	public Result deleteFile(String id) {
		//1、根据id获取文件信息
		FileInfo info = this.storageDao.findById(id).orElse(null);
		//2、删除硬盘上的文件
		if(info != null) {
			File file = new File(dir,info.getPath());
			file.delete();
			//3、删除文件信息
			this.storageDao.delete(info);
		}
		
		return null;
	}

	

}
