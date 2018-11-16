package org.fkjava.storage.dao;

import org.fkjava.identity.domain.User;
import org.fkjava.storage.domain.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageDao extends JpaRepository<FileInfo, String>{

	Page<FileInfo> findByOwner(User user, Pageable pageable);

	Page<FileInfo> findByOwnerAndNameContaining(User user, String keyword, Pageable pageable);
	
}
