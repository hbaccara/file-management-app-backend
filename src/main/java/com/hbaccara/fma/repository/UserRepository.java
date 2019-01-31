package com.hbaccara.fma.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.entities.User;

@Service
public interface UserRepository extends CrudRepository<User, Long>{
	
	public User findByUsername(String username);
	
	public User findByToken(String token);
    
	public List<User> findByUsernameContaining(String username);
}
