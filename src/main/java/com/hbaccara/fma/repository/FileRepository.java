package com.hbaccara.fma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.entities.File;

@Service
public interface FileRepository extends CrudRepository<File, Long> {

	@Query("SELECT file FROM File file WHERE parent.id = :id")
	public List<File> findAllElementsByFolder(Long id);
	
	@Query("SELECT file FROM File file WHERE parent.id IS NULL AND owner.id = :ownerId")
	public List<File> findAllRootElementsByOwner(Long ownerId);
	
	@Query("SELECT file FROM File file INNER JOIN file.sharedWith u WHERE u.id = :userId")
	public List<File> findSharedElementsByUser(Long userId);

}
