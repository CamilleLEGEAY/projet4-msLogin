package com.projet4.msLogin.controller;

import java.net.URI;
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
import com.projet4.msLogin.modele.User;

@RestController
@RequestMapping(path="/msLogin")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping(path="/newUser")
	public ResponseEntity<Void> createUser(@Valid @RequestBody User user){
		User userAdd = userRepository.save(user);
		if (userAdd == null)
            return ResponseEntity.noContent().build();

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userAdd.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
	}
	
	@GetMapping(path="/User/{email}&{password}")  
	public @ResponseBody User findByPasswordAndEmail(@PathVariable String email, @PathVariable String password) {		
		List<User> oa = userRepository.findByPasswordAndEmail(password, email);
		User user = new User();
		if (oa.isEmpty()) throw new UserException("Erreur d'autantification. Veuillez recommencer");
		else user = oa.get(0);
		return user;
	}
	
	@DeleteMapping(path="/supprUser/{id}")
	public void supprimerAssurer(@PathVariable Integer id){
		userRepository.deleteById(id);
	}
	
	@PutMapping(path="/updateUser")
	public void modifierAssurer(@RequestBody User user){
		userRepository.save(user);
	}
}
