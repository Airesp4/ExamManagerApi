package com.app.ExamManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ExamManager.model.Usuario;
import com.app.ExamManager.service.ServiceUsuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/auth")
public class ControllerAuth {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @PostMapping("/login")
    @Operation(summary = "Realiza login do usuário", description = "Valida as credenciais do usuário e retorna o token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
        @ApiResponse(responseCode = "400", description = "Dados obrigatórios ausentes"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        // Validação básica para verificar se os campos obrigatórios estão preenchidos
        if (usuario.getUsername() == null || usuario.getUsername().isEmpty() ||
            usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário e senha são obrigatórios");
        }

        try {
            // Autentica o usuário e gera o token
            String token = serviceUsuario.autenticarUsuario(usuario.getUsername(), usuario.getPassword());

            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }
}

