package com.hbaccara.fma.rest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbaccara.fma.controllers.UserController;
import com.hbaccara.fma.dto.UserDto;

@RestController
public class UserRestController {

	@Autowired
	private UserController userController;

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> findByUsernameContaining(@RequestParam(value = "username") String username) {

		try {
			List<UserDto> userDtos = userController.findByUsernameContaining(username);

			return ResponseEntity.status(HttpStatus.OK).body(userDtos);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/user")
	public ResponseEntity<UserDto> addUser(@RequestParam(value = "username") String username,
			@RequestParam("password") String password) {

		try {
			UserDto userDto = userController.addUser(username, password);

			return ResponseEntity.status(HttpStatus.OK).body(userDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/user/login")
	public ResponseEntity<UserDto> login(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password, HttpServletRequest request) {

		try {
			UserDto userDto = userController.login(username, password);
					
			return ResponseEntity.status(HttpStatus.OK).body(userDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/user/logout")
	public ResponseEntity<?> logout(@RequestParam(value = "userId") Long userId) {

		try {
			userController.logout(userId);

			return ResponseEntity.status(HttpStatus.OK).body(null);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
