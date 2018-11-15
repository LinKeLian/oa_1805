package org.fkjava.storage.service.impl;

import java.io.InputStream;

import org.fkjava.storage.domain.FileInfo;
import org.fkjava.storage.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService{
	
	private static final Logger LOG = LoggerFactory.getLogger(StorageServiceImpl.class); 
	
	@Override
	public Page<FileInfo> findFiles(String keyword, Integer number) {
		
		
		return null;
	}

	@Override
	public void save(FileInfo info, InputStream in) {
		
	}

}
