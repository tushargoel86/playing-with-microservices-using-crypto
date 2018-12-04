package com.tushar.crypto.endpoint;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tushar.crypto.service.ServiceLayer;

@RestController
public class Endpoint {
	
	
	@Autowired
	private ServiceLayer service;
	
	@GetMapping("/")
	public String home() {
		return "welcome to the home*****";
	}
	
	@PostMapping(("/decrypt/symmetric"))
	public ResponseEntity<String> decrypt(@RequestParam Map<String, String> request) {
		return service.decrypt(request);
	}
	
	@PostMapping("/encrypt/symmetric")
	public ResponseEntity<String> encrypt(@RequestParam Map<String, String> request) {
		return service.encrypt(request);
	}
 
}

