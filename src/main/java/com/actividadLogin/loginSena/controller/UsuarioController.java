package com.actividadLogin.loginSena.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.actividadLogin.loginSena.dto.RegistroUsuarioDTO;
import com.actividadLogin.loginSena.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/")
	public String index() {
		return "login";
	}

	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, 
            Model model) {
		  if (error != null) {
	            model.addAttribute("error", "Usuario o contraseña incorrectos");
	        }
	        return "login";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("userRegistrationDto", new RegistroUsuarioDTO());
		return "registro"; // Retorna la vista de registro
	}
	
	@GetMapping("/home")
	public String home(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			model.addAttribute("username", authentication.getName());
			return "inicio"; // Vista principal para el usuario autenticado
		} else {
			return "redirect:/login"; // Redirige al login si no está autenticado
		}
	}

	@PostMapping("/register")
	public String registerUser(@Valid RegistroUsuarioDTO userRegistrationDto, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "registro"; // Si hay errores de validación, retorna a la página de registro
		}

		boolean isRegistered = usuarioService.registerUser(userRegistrationDto);

		if (isRegistered) {
			model.addAttribute("successMessage", "Usuario registrado correctamente");
			return "login"; // Redirige al login si el registro es exitoso
		} else { 
			model.addAttribute("errorMessage", "El usuario ya existe");
			model.addAttribute("userRegistrationDto", userRegistrationDto); 
			return "registro"; // Muestra mensaje de error si el usuario ya existe
		}
	}


	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model model) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			return "redirect:/home"; // Redirige a la página de inicio

		} catch (BadCredentialsException e) {
			model.addAttribute("error", "Credenciales inválidas");
			return "login"; // Retorna a la página de login si falla
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/login"; // Redirige a la página de login después de cerrar sesión
	}

}
