package com.hbaccara.fma.rest.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbaccara.fma.dto.UserDto;
import com.hbaccara.fma.entities.User;
import com.hbaccara.fma.rest.services.AuthenticationRestService;

@RestController
public class AuthenticationRestController {

	@Autowired
	private AuthenticationRestService authenticationRestService;

	@PostMapping("/user/login")
	public ResponseEntity<UserDto> login(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password, HttpServletRequest request) {

		try {
			UserDto userDto = authenticationRestService.login(username, password);

			return ResponseEntity.status(HttpStatus.OK).body(userDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/user/logout")
	public ResponseEntity<?> logout(@AuthenticationPrincipal User user) {

		try {
			authenticationRestService.logout(user);

			return ResponseEntity.status(HttpStatus.OK).body(null);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
