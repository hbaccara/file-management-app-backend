package com.hbaccara.fma.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.dto.UserDto;
import com.hbaccara.fma.entities.User;
import com.hbaccara.fma.mappers.UserMapper;
import com.hbaccara.fma.repository.UserRepository;

@Service
public class UserController {

	@Autowired
	private UserRepository userRepository;

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
		user.setPassword(password);

		userRepository.save(user);

		UserDto userDto = userMapper.userToUserDto(user);

		return userDto;
	}

	public UserDto login(String username, String password) {

		User user = userRepository.findByUsernameAndPassword(username, password);

		UserDto userDto = null;

		if (user != null) {

			userDto = userMapper.userToUserDto(user);
		}

		return userDto;
	}

	public void logout(Long userId) {

		// TODO write logout logic
	}
}
