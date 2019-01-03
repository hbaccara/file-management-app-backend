package com.hbaccara.fma.rest.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.dto.UserDto;
import com.hbaccara.fma.entities.User;
import com.hbaccara.fma.mappers.UserMapper;
import com.hbaccara.fma.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserMapper userMapper;

	public List<UserDto> findByUsernameContaining(String username) {

		List<UserDto> userDtos = new ArrayList<>();

		userRepository.findByUsernameContaining(username).forEach(user -> {

			UserDto userDto = userMapper.userToUserDto(user);

			userDtos.add(userDto);
		});

		return userDtos;
	}

	public UserDto addUser(String username, String password) {

		User user = new User();

		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));

		userRepository.save(user);

		UserDto userDto = userMapper.userToUserDto(user);

		return userDto;
	}

	public UserDto login(String username, String password) {

		UserDto userDto = null;
		
		User user = userRepository.findByUsername(username);
		
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {

			userDto = userMapper.userToUserDto(user);
		}

		return userDto;
	}

	public void logout(Long userId) {

		// TODO write logout logic
	}
}
