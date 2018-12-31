package com.hbaccara.fma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.entities.User;

@Service
public interface UserRepository extends CrudRepository<User, Long>{

	@Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
	public User findByUsernameAndPassword(String username, String password);
    
	public List<User> findByUsernameContaining(String username);
}
