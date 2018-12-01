package com.tushar.crypto.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tushar.crypto.auth.filter.JwtAuthenticationFilter;
import com.tushar.crypto.auth.repository.DbServiceRepository;
import com.tushar.crypto.auth.service.CustomUserDetailsService;

/**
 * <p>
 * This class is annotated with @EnableWebSecurity to enable Spring Security’s 
 * web security support and provide the Spring MVC integration. It also extends 
 * WebSecurityConfigurerAdapter and overrides a couple of its methods to set 
 * some specifics of the web security configuration.
 * 
 * Security Config will:
 * 	<li> Require authentication to every URL in your application </li>
 *  <li> Allow the user with the Username user and the Password password to
 *  	 authenticate </li>
 *  <li> CSRF attack prevention </li>
 *  <li> Session Fixation protection </li>
 *  
 *  This class can not directly accessed by outside. All request must goes to
 *  API gateway and on gateway all role bases access and authentication
 *  rules are configured.
 *  
 *  In this class we disable CORS and CSRF, enable all users to send create
 *  user and token request (JWT).
 *  
 *  Also we have added Custom filter to authenticate the user and once authenticated
 *  a JWT will be returned.
 *  </p>
 * @author Tushar Goel
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

	@Autowired
	private DbServiceRepository dbServiceRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
 		 http
		 	//disabling cross region resource sharing and cross site  regions
		 	.cors().and().csrf().disable() 
			 // make sure we use stateless session; session won't be used to store user's state.
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			//allow all to send create user request
		    .antMatchers("/api/createUser").permitAll() 
		    //permit all to request token
		 	.antMatchers(HttpMethod.POST, jwtConfig().getUri().trim()).permitAll() 
			//any other request must be authenticated
			.anyRequest().authenticated()
			.and()
			// Add a filter to validate user credentials and add token in the response header
		    // What's the authenticationManager()? 
		    // An object provided by WebSecurityConfigurerAdapter, used to authenticate the 
			//user passing user's credentials
		    // The filter needs this auth manager to authenticate the user. We are accessing
			// this through overrided method authenticationManager()
			.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfig()));
	}

	
	/**
	 * Authentication Manager allows for easily building for memory authentication
	 * LDAP authentication, JDBC based authentication, adding UserDetailsService, 
	 * and adding AuthenticationProvider's.
	 * 
	 * Adding custom user service class to fetch user details by itself. 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.userDetailsService(new CustomUserDetailsService(dbServiceRepository));
	}
	
	
	/**
	 * Class to configure JWT uses for authentication and authorization.
	 */
	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}
	
	/**
	 * In memory password is stored in encrypted format. This default
	 * implementation will helps to encrypt the password instead storing it
	 * in plaintext.
	 */
	@Bean
    public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder(12);
	}

}
