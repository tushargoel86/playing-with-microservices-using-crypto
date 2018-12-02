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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * uses when authenticated user doesn't have permission to use
 * any particular resources.
 * */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		Map<String, Object> data = new HashMap<>();
		data.put("timestamp", new Date());
		data.put("status", HttpStatus.UNAUTHORIZED.value());
		data.put("message", "User is not authorized to use this feature");
		data.put("path", request.getRequestURL().toString());

		OutputStream out = response.getOutputStream();
		com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, data);
		out.flush();
	}

}
