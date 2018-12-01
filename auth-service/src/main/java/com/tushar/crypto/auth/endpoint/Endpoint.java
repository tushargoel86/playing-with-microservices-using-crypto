package com.tushar.crypto.auth.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tushar.crypto.auth.model.ApplicationUser;
import com.tushar.crypto.auth.model.Role;
import com.tushar.crypto.auth.repository.DbServiceRepository;

@RestController
public class Endpoint {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DbServiceRepository dbServiceRepository;
	
	@PostMapping("/api/createUser")
	public String createUser(String username, String password, String role) {
		List<Role> userRole = new ArrayList<>();
		userRole.add(Role.valueOf(role));
		
		//spring 5 stores the password in encoded format. We convert the same.
		ApplicationUser applicationUser = new ApplicationUser(username, passwordEncoder.encode(password), role);
		dbServiceRepository.save(applicationUser);
		return "user created";
	}
	
	@GetMapping("/api/welcome")
	public String welcome() {
		return "Welcome to the Hell";
	}
}
