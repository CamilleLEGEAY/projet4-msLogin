package com.projet4.msLogin.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.projet4.msLogin.modele.User;

public interface UserRepository extends CrudRepository<User, Integer>{

	List<User> findByPasswordAndEmail(String password, String email);
	
	List<User> findByPasswordAndUsername(String password, String username);
	
}