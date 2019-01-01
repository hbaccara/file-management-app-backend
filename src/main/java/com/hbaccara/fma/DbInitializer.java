package com.hbaccara.fma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hbaccara.fma.repository.UserRepository;
import com.hbaccara.fma.entities.User;

/**
 * Database initializer that populates the database with some initial user
 * accounts using JPA.
 * 
 * This component is started only when app.db-init property is set to true
 */

@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DbInitializer implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... strings) throws Exception {

		User user1 = new User("hamdi", passwordEncoder.encode("hamdi"));
		User user2 = new User("ahmed", passwordEncoder.encode("ahmed"));

		userRepository.save(user1);
		userRepository.save(user2);

		System.out.println("Database has been initialized");
	}
}
