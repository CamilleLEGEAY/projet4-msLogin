package com.projet4.msLogin.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projet4.msLogin.dao.UserRepository;
import com.projet4.msLogin.modele.Login;


@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Login> optionalIdentity = userRepo.findByEmail(email);
		if (optionalIdentity.isPresent()) {
			Login identity = optionalIdentity.get();
			return new User(identity.getEmail(), identity.getPassword(),
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
	}


}
