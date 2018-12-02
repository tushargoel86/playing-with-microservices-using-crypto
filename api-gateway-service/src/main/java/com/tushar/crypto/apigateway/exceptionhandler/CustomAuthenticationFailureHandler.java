package com.tushar.crypto.apigateway.exceptionhandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handle the case when user credential are wrong.
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		response.setStatus(HttpStatus.FORBIDDEN.value());

		Map<String, Object> data = new HashMap<>();
		data.put("timestamp", new Date());
		data.put("status", HttpStatus.FORBIDDEN.value());
		data.put("message", "Invalid credential");
		data.put("path", request.getRequestURL().toString());

		OutputStream out = response.getOutputStream();
		com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, data);
		out.flush();
	}
}