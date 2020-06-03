package com.projet4.msLogin.modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Length(min=8, max=15, message="Le nombre de caractères doit être compris entre 8 et 15")
	private String password;
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Email
	private String email;
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Length(min=2, max=30, message="Le nombre de caractères doit être compris entre 2 et 30")
	private String userName;

	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getId() {
		return id;
	}

}	