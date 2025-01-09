package com.app.ExamManager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz ->
                authz
                    .requestMatchers("/login", "/register").permitAll()  // Permitir acesso sem autenticação a essas páginas
                    .anyRequest().authenticated()  // Requer autenticação para todas as outras páginas
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")  // Página de login personalizada
                    .permitAll()  // Permitir que qualquer um acesse a página de login
            )
            .logout(logout ->
                logout
                    .permitAll()  // Permitir que qualquer um faça logout
            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usar o BCrypt para codificar senhas
    }
}

