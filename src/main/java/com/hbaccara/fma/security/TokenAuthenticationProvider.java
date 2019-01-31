package com.hbaccara.fma.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private AuthenticationService auth;

	@Override
	protected void additionalAuthenticationChecks(UserDetails d, UsernamePasswordAuthenticationToken auth) {
		// Nothing to do
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication) {
		Object token = authentication.getCredentials();
		return Optional.ofNullable(token).map(String::valueOf).map(auth::findByToken).orElseThrow(
				() -> new UsernameNotFoundException("Cannot find user with authentication token=" + token));
	}
}