package com.tushar.crypto.auth.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tushar.crypto.auth.model.ApplicationUser;
import com.tushar.crypto.auth.repository.DbServiceRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private DbServiceRepository dbServiceRepository;

	public CustomUserDetailsService() {
	}
	
	public CustomUserDetailsService(DbServiceRepository dbServiceRepository) {
		this.dbServiceRepository = dbServiceRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		  //fetching user details from the repository (database here)
		  ApplicationUser user = dbServiceRepository.findByUserName(username);
		  
		  if (user == null)
			  throw new UsernameNotFoundException(username + " not found");
		  
		//Spring needs roles to be in this format: "ROLE_" + userRole (i.e. "ROLE_USER")
		// So, we need to set it to that format, so we can verify and compare roles (i.e. hasRole("USER")).
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
	                	.commaSeparatedStringToAuthorityList("ROLE_" +user.getRole());
			
		  return new User(user.getUserName(), user.getPassword(), grantedAuthorities);  
	}

}
