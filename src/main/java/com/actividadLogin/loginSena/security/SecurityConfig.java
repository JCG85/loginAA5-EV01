package com.actividadLogin.loginSena.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	 @Autowired
	    private CustomUserDetailsService userDetailsService;
	 
	 @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	        return authConfig.getAuthenticationManager();
	    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {	
		 http
		 	.csrf().disable()
	        .authorizeHttpRequests(authz -> authz
	            .requestMatchers("/", "/register", "/login").permitAll() // Permite el acceso sin autenticación
	            .anyRequest().authenticated() // Requiere autenticación para cualquier otra URL
	        )
	        .formLogin(form -> form
	            .loginPage("/login") // Página de login personalizada
	            .defaultSuccessUrl("/home", true) // Redirige a /home tras un login exitoso
	            .permitAll() // Permite el acceso a la página de login
	        )
	        .logout(logout -> logout
	        	.logoutUrl("/logout")
	        	.logoutSuccessUrl("/login")
	        	.permitAll());
	  
	    return http.build();
	}
	
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Utiliza BCrypt para codificar contraseñas
	}
	
	
	
}
