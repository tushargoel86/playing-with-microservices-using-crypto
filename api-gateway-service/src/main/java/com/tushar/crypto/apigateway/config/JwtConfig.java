package com.tushar.crypto.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
public final class JwtConfig {

	@Value("${netflix-zul-service.securityURI:/auth/token}")
	private String uri;

	@Value("${netflix-zul-service.authHeader:Authorization}")
	private String header;

	@Value("${netflix-zul-service.prefix:Bearer}")
	private String prefix;

	@Value("${netflix-zul-service.secret:secre}")
	private String secret;

	@Value("${netflix-zul-service.expirationTime:3600}")
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
