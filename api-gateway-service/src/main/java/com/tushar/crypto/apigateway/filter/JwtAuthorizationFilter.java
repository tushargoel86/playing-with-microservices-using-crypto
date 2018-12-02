package com.tushar.crypto.apigateway.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tushar.crypto.apigateway.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;


public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	private JwtConfig jwtConfig;
	
	public JwtAuthorizationFilter(JwtConfig jwtConfig) {
		super();
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
						HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		//get the header from the request
		String header = request.getHeader(jwtConfig.getHeader().trim());

		//validates the header and check the prefix
		if (header == null || !header.startsWith(jwtConfig.getPrefix().trim())) {
			chain.doFilter(request, response);
			return;
		}
			
		//get the token
		String token = header.replace(jwtConfig.getPrefix().trim(), "");
		
		try {
			//validates token
			Claims claims = Jwts.parser()
				.setSigningKey(jwtConfig.getSecret())
				.parseClaimsJws(token)
				.getBody();
			
			String username = claims.getSubject();
			if(username != null) {
				@SuppressWarnings("unchecked")
				List<String> authorities = (List<String>) claims.get("scopes");

				// 5. Create auth object
				// UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
				// It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
				 UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							 username, "", authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
				 
				 // 6. Authenticate the user
				 // Now, user is authenticated
				 SecurityContextHolder.getContext().setAuthentication(auth);
			}   
			} catch (ExpiredJwtException | SignatureException e) { 
				expiredJWTException(response, e.getMessage());
				SecurityContextHolder.clearContext();
			} catch (Exception e) {
				SecurityContextHolder.clearContext();
		}
		chain.doFilter(request, response);
	}

	/**
	 * Setting response status for user with insufficient permission.
	 */
	private void expiredJWTException(HttpServletResponse response, String msg) throws IOException {
		response.setStatus(HttpStatus.SC_UNAUTHORIZED);

		Map<String, Object> data = new HashMap<>();
		data.put("timestamp", new Date());
		data.put("status", HttpStatus.SC_UNAUTHORIZED);
		data.put("message", msg);

		OutputStream out = response.getOutputStream();
		com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, data);
		out.flush();
	}

	
}
