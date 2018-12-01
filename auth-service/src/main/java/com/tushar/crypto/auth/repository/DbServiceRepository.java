package com.tushar.crypto.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tushar.crypto.auth.model.ApplicationUser;

@Repository
public interface DbServiceRepository extends CrudRepository<ApplicationUser,  Long> {

		ApplicationUser findByUserName(String username);
		
}
