package com.tushar.crypto.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
public final class JwtConfig {

	@Value("${auth-service.uri}")
	private String uri;

	@Value("${auth-service.authHeader}")
	private String header;

	@Value("${auth-service.prefix:Bearer}")
	private String prefix;

	@Value("${auth-service.secret:secre}")
	private String secret;

	@Value("${auth-service.expirationTime:3600}")
	private int expirationTime;

	public String getUri() {
		return uri;
	}

	public String getHeader() {
		return header;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSecret() {
		return secret;
	}

	public int getExpirationTime() {
		return expirationTime;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setExpirationTime(int expirationTime) {
		this.expirationTime = expirationTime;
	}

	
}
