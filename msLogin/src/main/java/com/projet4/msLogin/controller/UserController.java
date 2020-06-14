package com.projet4.msLogin.controller;

import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projet4.msLogin.dao.UserRepository;
import com.projet4.msLogin.exception.UserException;
import com.projet4.msLogin.modele.LoginResponse;
import com.projet4.msLogin.modele.User;


@RestController
@RequestMapping(path="/msLogin")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * creat a new user
	 * @param user
	 * @return
	 */
	@PostMapping(path="/public/newUser")
	public LoginResponse createUser(@Valid @RequestBody User user){
		User userAdd = userRepository.save(user);
		LoginResponse loginResponse = new LoginResponse();
		if (userAdd == null) {
			loginResponse.setMessage("Erreur d'autantification.");
		}
		loginResponse.setMessage("Bonjour "+user.getUsername());
		loginResponse.setUsername(user.getUsername());
		loginResponse.setToken("got it"/*newJWTtoken(user)*/);
        return loginResponse;
	}
	/**
	 * to login an user
	 * @param email
	 * @param password
	 * @return a token and a message
	 */
	@PostMapping(path="/public/User/login")  
	public LoginResponse findByPasswordAndUsername(@Valid @RequestBody User user) {		
		List<User> oa = userRepository.findByPasswordAndUsername(user.getPassword(), user.getUsername());
		LoginResponse loginResponse = new LoginResponse();
		User userLogged = new User();
		if (oa.isEmpty()) {
			loginResponse.setMessage("Vous avez saisi un username ou un mot de passe incorrects");
			//throw new UserException("Erreur d'autantification");
		}
		else {userLogged = oa.get(0);
		loginResponse.setMessage("Bonjour "+userLogged.getUsername());
		loginResponse.setUsername(userLogged.getUsername());
		loginResponse.setToken("got it"/*newJWTtoken(user)*/);
		}
		return loginResponse;
	}
	/**
	 * to delete an user (only if we get the id in the DB (maybe return it in the token))
	 * @param id
	 */
	@DeleteMapping(path="/supprUser/{id}")
	public void supprimerAssurer(@PathVariable Integer id){
		userRepository.deleteById(id);
	}
	/**
	 * to update an user (only if we get the id in the DB (maybe return it in the token))
	 * @param id
	 */
	@PutMapping(path="/updateUser")
	public void modifierAssurer(@RequestBody User user){
		userRepository.save(user);
	}
	/**
	 * to generate a token
	 */
//	private String newJWTtoken(User user) {
//		String token = Jwts.builder()
//			    .setClaims("msLogin")
//			    .setSubject(email)
//			    .setIssuedAt(Date.from(Instant.now()))
//			    .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)))
//			    .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
//			    .compact();
//		return token;
//	}
}
