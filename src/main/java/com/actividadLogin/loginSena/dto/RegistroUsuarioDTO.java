package com.actividadLogin.loginSena.dto;

import jakarta.validation.constraints.NotEmpty;

public class RegistroUsuarioDTO {
	@NotEmpty(message = "username no debe estar vacio")
	 private String username;
	@NotEmpty(message = "Constraseña no debe estar vacio")
	 private String password;
	 
	 
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	 
}
