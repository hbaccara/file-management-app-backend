package com.hbaccara.fma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.entities.File;

@Service
public interface FileRepository extends CrudRepository<File, Long> {

	@Query("SELECT file FROM File file WHERE parent.id = :id")
	public List<File> findAllFilesByFolder(Long id);
	
	@Query("SELECT file FROM File file WHERE parent.id IS NULL AND owner.id = :ownerId")
	public List<File> findAllRootFilesByOwner(Long ownerId);
	
	@Query("SELECT file FROM File file INNER JOIN file.sharedWith u WHERE u.id = :userId")
	public List<File> findFilesSharedWithUser(Long userId);
	
	@Query("SELECT file FROM File file WHERE owner.id = :ownerId AND UPPER(name) LIKE CONCAT('%',UPPER(:searchTerm),'%')")
	public List<File> findFilesByOwnerAndSearchTerm(Long ownerId, String searchTerm);
	
	@Query("SELECT file FROM File file INNER JOIN file.sharedWith u WHERE u.id = :userId AND UPPER(file.name) LIKE CONCAT('%',UPPER(:searchTerm),'%')")
	public List<File> findFilesSharedWithUserBySearchTerm(Long userId, String searchTerm);

}
