package com.hbaccara.fma.security;

import com.hbaccara.fma.entities.User;

public interface AuthenticationService {

	User login(String username, String password);

	User findByToken(String token);

	void logout(User user);
}