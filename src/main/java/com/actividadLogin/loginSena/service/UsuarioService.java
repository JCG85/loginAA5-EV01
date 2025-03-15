package com.actividadLogin.loginSena.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.actividadLogin.loginSena.dto.RegistroUsuarioDTO;
import com.actividadLogin.loginSena.model.Usuario;
import com.actividadLogin.loginSena.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private UsuarioRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public UsuarioService(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	//metodo registrar.
	public boolean registerUser(RegistroUsuarioDTO userDto) {
		Optional<Usuario> existingUser = userRepository.findByUsername(userDto.getUsername());

		if (existingUser.isPresent()) {
			return false;
		}
		Usuario user = new Usuario();
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		userRepository.save(user);
		return true;

	}
	
	  

}
