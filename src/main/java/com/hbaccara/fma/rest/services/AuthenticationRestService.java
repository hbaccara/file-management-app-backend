package com.hbaccara.fma.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.dto.UserDto;
import com.hbaccara.fma.entities.User;
import com.hbaccara.fma.mappers.UserMapper;
import com.hbaccara.fma.repository.UserRepository;
import com.hbaccara.fma.security.UUIDAuthenticationService;

@Service
public class AuthenticationRestService {

	@Autowired
	UUIDAuthenticationService uuidAuthenticationService;

	UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	public UserDto login(String username, String password) {

		UserDto userDto = null;

		User user = uuidAuthenticationService.login(username, password);

		if (user != null) {

			userDto = userMapper.userToUserDto(user);
		}

		return userDto;
	}

	public User findByToken(String token) {
		return uuidAuthenticationService.findByToken(token);
	}

	public void logout(User user) {
		uuidAuthenticationService.logout(user);
	}
}