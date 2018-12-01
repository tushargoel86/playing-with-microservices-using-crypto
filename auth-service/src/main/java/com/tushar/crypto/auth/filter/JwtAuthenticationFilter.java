package com.tushar.crypto.auth.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tushar.crypto.auth.config.JwtConfig;
import com.tushar.crypto.auth.model.Credentials;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * This filter will authenticate the user credential while requesting the token
 * request, if credential is valid than token is returned.
 *
 * Also by default, path for requesting token is '/login'. We need to change to
 * custom path.
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JwtConfig jwtConfig;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager
								, JwtConfig jwtConfig) {
		this.authenticationManager = authenticationManager;
		this.jwtConfig = jwtConfig;

		// By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
		// In our case, we need to override the defaults.
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(
									jwtConfig.getUri().trim(), "POST"));

	}

	/**
	 * This method will validate the credential. First convert the JSON request to User
	 * object and then validate the credentials.
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
						HttpServletResponse response) throws AuthenticationException {
		Authentication authentication = null;

		try {
			// convert json request to an object
			Credentials credentials = new ObjectMapper()
										.readValue(request.getInputStream(), Credentials.class);

			// authenticate the credentials
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credentials.getUsername(),
															credentials.getPassword()));
		} catch (UsernameNotFoundException | IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		return authentication;
	}
	
	/**
	 * now i am authenticated and needs my token for authorizations.
	 * structure of JWT:
	 * 	1) Expiry time
	 *  2) Username
	 *  3) Role assign to the user
	 *  4) Secret data
	 * Once this done we need to sign it with signature algorithm.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {

		ZonedDateTime expirationTime = ZonedDateTime.now(ZoneOffset.UTC).plus(jwtConfig.getExpirationTime(),
				ChronoUnit.MILLIS);


		//creating claim by fetching user name
		Claims claims = Jwts.claims()
							.setSubject(authentication.getName());
		
		//fetching roles assigned to user
		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		
		//adding roles in the claim
		claims.put("scopes", roles);

		//creating JWT from expiration time, claims, secret and signing with HS512 algo 
		String token = Jwts.builder()
							.setClaims(claims)
							.setExpiration(Date.from(expirationTime.toInstant()))
							.signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtConfig.getSecret())
							.compact();

		// add token to response. may reate json format for token and expiration time
		response.getWriter().write(token);

		// or

		// add token to the response header
		// response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpStatus.FORBIDDEN.value());

		Map<String, Object> data = new HashMap<>();
		data.put("timestamp", new Date());
		data.put("status", HttpStatus.FORBIDDEN.value());
		data.put("message", "Invalid credentials");
		data.put("path", request.getRequestURL().toString());

		OutputStream out = response.getOutputStream();
		com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, data);
		out.flush();
	}
}
