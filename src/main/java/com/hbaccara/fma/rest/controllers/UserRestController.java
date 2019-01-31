package com.hbaccara.fma.rest.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbaccara.fma.dto.UserDto;
import com.hbaccara.fma.rest.services.UserRestService;

@RestController
public class UserRestController {

	@Autowired
	private UserRestService userRestService;

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> findByUsernameContaining(@RequestParam(value = "username") String username) {

		try {
			List<UserDto> userDtos = userRestService.findByUsernameContaining(username);

			return ResponseEntity.status(HttpStatus.OK).body(userDtos);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/user/register")
	public ResponseEntity<UserDto> addUser(@RequestParam(value = "username") String username,
			@RequestParam("password") String password) {

		try {
			UserDto userDto = userRestService.addUser(username, password);

			return ResponseEntity.status(HttpStatus.OK).body(userDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
