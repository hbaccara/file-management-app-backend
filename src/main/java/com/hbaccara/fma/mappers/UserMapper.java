package com.hbaccara.fma.mappers;

import org.springframework.stereotype.Service;

import com.hbaccara.fma.dto.UserDto;
import com.hbaccara.fma.entities.User;

@Service
public class UserMapper {

	public UserDto userToUserDto(User user) {
		
		UserDto userDto = new UserDto();
		
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		userDto.setToken(user.getToken());
		
		return userDto;
	}
	
}
