package com.tushar.crypto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class Config {

	@Value("${crypto-service.keybytes}")
	private String keyBytes;
	
	@Value("${crypto-service.encoding:UTF-8}")
	private String encoding;

	public String getKeyBytes() {
		return keyBytes;
	}

	public void setKeyBytes(String keyBytes) {
		this.keyBytes = keyBytes;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	

}
