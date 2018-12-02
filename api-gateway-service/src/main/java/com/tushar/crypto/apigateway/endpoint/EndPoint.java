package com.tushar.crypto.apigateway.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class EndPoint {

	@Autowired
	private CreateUserProxy proxy;
	
	@PostMapping("/createUser")
	@HystrixCommand(fallbackMethod="unableToCreateUser")
	public String createUser(HttpServletRequest request,  String username, String password, String role) {
		try {
			return proxy.createUser(request.getHeader("Authorization"), username, password, role);
		} catch (Exception e)  {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
	
	private String unableToCreateUser(HttpServletRequest request,  String username, String password, String role) {
		return "unable to create user";
	}
}
