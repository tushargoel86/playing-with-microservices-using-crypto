package com.tushar.crypto.service;

import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tushar.crypto.config.Config;

@Service
public class ServiceLayer {

	@Autowired
	private Config config;

	public ResponseEntity<String> decrypt(Map<String, String> request) {
		ResponseEntity<String> entity = null;
		try {
			//initializing cipher with given transformation 
			Cipher cipher = Cipher.getInstance(request.get("transformation"), "SunJCE");
			
			//creating key object using key set in the config server
			SecretKeySpec key = new SecretKeySpec(DatatypeConverter.parseHexBinary(config.getKeyBytes()), "AES");
			if (request.get("iv") != null) {
				cipher.init(Cipher.DECRYPT_MODE, key,
						new IvParameterSpec(DatatypeConverter.parseHexBinary(request.get("iv"))));
			} else {
				cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
			}
			
			//decrypting the cipher text with the given padding and key.  
			String output =  new String(cipher.doFinal(DatatypeConverter.parseHexBinary(request.get("ciphertext"))), "UTF-8");
			entity = new ResponseEntity<String>(output, HttpStatus.OK);
		} catch (Exception e) {
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}

	public ResponseEntity<String> encrypt(Map<String, String> request) {
		ResponseEntity<String> entity = null;
		try {
			Cipher cipher = Cipher.getInstance(request.get("transformation"), "SunJCE");
			SecretKeySpec key = new SecretKeySpec(DatatypeConverter.parseHexBinary(config.getKeyBytes()), "AES");
			
			if (request.get("iv") != null) {
				cipher.init(Cipher.ENCRYPT_MODE, key,
						new IvParameterSpec(DatatypeConverter.parseHexBinary(request.get("iv"))));
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
			}
			
			String output = DatatypeConverter.printHexBinary(cipher.doFinal(request.get("data").getBytes()));
			entity = new ResponseEntity<String>(output, HttpStatus.OK);
		} catch (Exception e) {
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}

}
