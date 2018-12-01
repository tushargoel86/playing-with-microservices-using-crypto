package com.tushar.crypto.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @SpringBootApplication is a convenience annotation that adds all of the
 *                        following:
 * @Configuration tags the class as a source of bean definitions for the
 *                application context.
 * @EnableAutoConfiguration tells Spring Boot to start adding beans based on
 *                          classpath settings, other beans, and various
 *                          property settings.
 * 
 *                          Normally you would add @EnableWebMvc for a Spring
 *                          MVC app, but Spring Boot adds it automatically when
 *                          it sees spring-webmvc on the classpath. This flags
 *                          the application as a web application and activates
 *                          key behaviors such as setting up a
 *                          DispatcherServlet.
 * 
 * @ComponentScan tells Spring to look for other components, configurations, and
 *                services in the hello package, allowing it to find the
 *                controllers.
 *
 * @author tgoel
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

	/**
	 * The authentication flow is simple as:
	 * 
	 * The user sends a request to get a token passing his credentials. The server
	 * validates the credentials and sends back a token. With every request, the
	 * user has to provide the token, and server will validate that token.
	 *
	 * We will have AuthService for validating user credential and issuing tokens.
	 * 
	 * What about validation? Validation of token can be done at 2 places: 
	 * 
	 * At Auth Service: In this Gateway has to call the auth service to validate the
	 * 					token before allowing the requests to go to any service.
	 *
	 * At the Gateway level: Let the auth service validate user credentials, and
	 * 					issue tokens.
	 * 
	 * In both ways, we are blocking the requests unless it’s authenticated (except
	 * the requests for generating tokens). Here we are going with 2nd way.
	 */
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}
}
