package com.tushar.crypto.apigateway.config;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tushar.crypto.apigateway.exceptionhandler.CustomAccessDeniedHandler;
import com.tushar.crypto.apigateway.exceptionhandler.CustomAuthenticationEntryPointHandler;
import com.tushar.crypto.apigateway.exceptionhandler.CustomAuthenticationFailureHandler;
import com.tushar.crypto.apigateway.exceptionhandler.ExceptionHandlerFilter;
import com.tushar.crypto.apigateway.filter.JwtAuthorizationFilter;

import feign.Request;

@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()   
			.authorizeRequests() //authorizing request
			//allowing all users to send token request
			.antMatchers(HttpMethod.POST, jwtConfig().getUri().trim()).permitAll()
			//for debugging purpose only
			.antMatchers(HttpMethod.GET, "/crypto/").permitAll()
			//only user has admin role can create new user
			.antMatchers("/createUser").hasRole("ADMIN")
			//rest all request should be authenticated
			.anyRequest().authenticated()
			.and()
			//adding filter for validating token and authorizing user credentials
			.addFilterAfter(new JwtAuthorizationFilter(jwtConfig()), UsernamePasswordAuthenticationFilter.class)
			//custom exception handling in case authenticated user doesn't have access to some 
			//of the resources it wants to access
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
			//when credential are not validated
			.authenticationEntryPoint(customAuthenticationEntryPointHandler()); 
		
		//http.formLogin().failureHandler(customAuthenticationFailureHandler());
	}
	
	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Bean
	public AuthenticationEntryPoint customAuthenticationEntryPointHandler() {
		return new CustomAuthenticationEntryPointHandler();
	}
	
	@Bean
	public AuthenticationFailureHandler customAuthenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}
	
	/**
     * Method to create a bean to increase the timeout value, 
     * It is used to overcome the Retryable exception while invoking the feign client.
     */
	@Bean
    public static Request.Options requestOptions(ConfigurableEnvironment env) {
        int ribbonReadTimeout = env.getProperty("ribbon.ReadTimeout", int.class, 70000);
        int ribbonConnectionTimeout = env.getProperty("ribbon.ConnectTimeout", int.class, 60000);

        return new Request.Options(ribbonConnectionTimeout, ribbonReadTimeout);
    }
}
