package com.tushar.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CrypoServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CrypoServiceApplication.class, args);
	}
}
