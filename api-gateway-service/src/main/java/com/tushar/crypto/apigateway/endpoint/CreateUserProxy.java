package com.tushar.crypto.apigateway.endpoint;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name="netflix-zul-service")
@RibbonClient(name="auth-service")
@EnableDiscoveryClient
public interface CreateUserProxy {

	@PostMapping("/auth-service/api/createUser")
	public String createUser(@RequestHeader("Authorization") String header, 
			@RequestParam("username") String username, 
			@RequestParam("password") String password,
			@RequestParam("role")String role);
}
