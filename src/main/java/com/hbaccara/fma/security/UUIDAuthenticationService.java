package com.hbaccara.fma.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.entities.User;
import com.hbaccara.fma.repository.UserRepository;

@Service
public class UUIDAuthenticationService implements AuthenticationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User login(String username, String password) {

		User user = userRepository.findByUsername(username);

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {

			// generate a security token for the user
			String uuid = UUID.randomUUID().toString();

			// save the security token
			user.setToken(uuid);
			userRepository.save(user);
		}

		return user;
	}

	@Override
	public User findByToken(String token) {
		return userRepository.findByToken(token);
	}

	@Override
	public void logout(User user) {

		// delete the security token
		user.setToken(null);
		userRepository.save(user);
	}
}