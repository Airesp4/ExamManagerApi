package com.app.ExamManager.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ExamManager.DTO.AuthenticationDTO;
import com.app.ExamManager.DTO.LoginResponseDTO;
import com.app.ExamManager.DTO.RegisterDTO;
import com.app.ExamManager.model.Usuario;
import com.app.ExamManager.repository.RepositoryUsuario;
import com.app.ExamManager.service.ServiceToken;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class ControllerAuth {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @Autowired
    private ServiceToken serviceToken;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = serviceToken.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){

        if (this.repositoryUsuario.findByUsername(data.login()) != null) 
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe");
        
        if (data.dateBirth() == null || data.dateBirth().isEmpty()) {
            return ResponseEntity.badRequest().body("Data de nascimento não pode ser vazia");
        }

        try {
            LocalDate dateBirth = LocalDate.parse(data.dateBirth());

            if (dateBirth.isAfter(LocalDate.now())) {
                return ResponseEntity.badRequest().body("Data de nascimento não pode ser futura");
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

            Usuario newUser = new Usuario(data.login(), encryptedPassword, dateBirth, data.role());
            this.repositoryUsuario.save(newUser);

            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
}