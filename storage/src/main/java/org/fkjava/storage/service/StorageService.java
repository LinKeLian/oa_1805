package org.fkjava.storage.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.fkjava.common.data.domain.Result;
import org.fkjava.storage.domain.FileInfo;
import org.springframework.data.domain.Page;

public interface StorageService {

	Page<FileInfo> findFiles(String keyword, Integer number);

	void save(FileInfo info, InputStream in);

	FileInfo findById(String id);

	InputStream getFileContent(FileInfo fi)throws FileNotFoundException;

	Result deleteFile(String id);
	
}
