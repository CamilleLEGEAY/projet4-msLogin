package com.projet4.msLogin.controller;

import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projet4.msLogin.dao.UserRepository;
import com.projet4.msLogin.modele.LoginResponse;
import com.projet4.msLogin.modele.LoginUpdate;
import com.projet4.msLogin.service.JwtUtilService;
import com.projet4.msLogin.modele.Login;

@RestController
@RequestMapping(path = "/msLogin")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;
	
	@Autowired
	private JwtUtilService jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * to create a new user and login it
	 * @param user
	 * @return a token and a message
	 */
	@PostMapping(path = "/public/newUser")
	public LoginResponse createUser(@Valid @RequestBody Login user)throws Exception {
		LoginResponse loginResponse = new LoginResponse();	
		String tempEmail = user.getEmail();
		if (tempEmail != null && !"".equals(tempEmail) && user.getPassword()!=null) {
			if (userRepository.findByEmail(tempEmail).isPresent()) {
				loginResponse.setMessage("e-mail already used");
				//throw new UserException("e-mail already used");
			} else {
				Login userAdd = user;
				userAdd.setPassword(passwordEncoder.encode(user.getPassword()));
				userAdd = userRepository.save(userAdd);
				loginResponse = this.createLoginResponse(userAdd);
			}
		} else {
			loginResponse.setMessage("saisie invalide");
			//throw new UserException("saisie invalide");
		}
		return loginResponse;
	}


	/**
	 * to login an user
	 * @param email
	 * @param password
	 * @return a token and a message
	 */
	@PostMapping(path = "/public/login")
	public LoginResponse login(@Valid @RequestBody Login user) throws Exception{
		LoginResponse loginResponse = new LoginResponse();
		authenticate(user.getEmail(), user.getPassword());
		loginResponse = this.createLoginResponse(user);	
		return loginResponse;
	}
	

	/**
	 * to delete an user . find his id from the token
	 * @param String token
	 */
	@DeleteMapping(path = "/supprUser")
	public String supprimerAssurer(@RequestBody String token) {
		String subject = jwtUtil.getUsernameFromToken(token.substring(7));
		userRepository.deleteById(userRepository.findByEmail(subject).get().getId());
		return "compte supprimé";
	}

	/**
	 * to update an user . find his id from the token
	 * 
	 * @param id
	 */
	@PutMapping(path = "/updateUser")
	public String updateUser(@RequestBody LoginUpdate userUpdate) {
		String subject = jwtUtil.getUsernameFromToken(userUpdate.getToken());
		Login user = userRepository.findByEmail(subject).get();
//		if(userUpdate.getPassword()!= null) {
//			user.setPassword(userUpdate.getPassword());
//		}
		if(userUpdate.getUsername()!= null) {
			user.setUserName(userUpdate.getUsername());
		}
		userRepository.save(user);
		return "compte mis à jour";
	}

	/**
	 * to compose a LoginResponse
	 * @param user
	 * @return loginResponse with token
	 */
	private LoginResponse createLoginResponse(Login user)throws Exception {
		LoginResponse loginResponse = new LoginResponse();
		//recuperer dans la DB pour créer un token
		final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(user.getEmail());
		final String token = jwtUtil.generateToken(userDetails);
		loginResponse.setToken(token);
		loginResponse.setMessage("Bonjour " + userDetails.getUsername());
		loginResponse.setUsername(userRepository.findByEmail(user.getEmail()).get().getUsername());
		return loginResponse;
	}
	
	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
