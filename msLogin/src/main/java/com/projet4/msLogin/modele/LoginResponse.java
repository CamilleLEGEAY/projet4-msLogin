package com.projet4.msLogin.modele;

public class LoginResponse {
	
	private String token;
	private String message;
	private String username;
	
	public LoginResponse() {}
	
	public LoginResponse(String token, String message, String username) {
		this.token = token;
		this.message = message;
		this.username = username;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
