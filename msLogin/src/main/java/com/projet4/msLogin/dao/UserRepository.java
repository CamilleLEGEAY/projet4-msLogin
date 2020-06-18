package com.projet4.msLogin.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.projet4.msLogin.modele.Login;

public interface UserRepository extends CrudRepository<Login, Integer>{

	Optional<Login> findByPasswordAndEmail(String password, String email);
	
	Optional<Login> findByEmail(String emailId);
	
}