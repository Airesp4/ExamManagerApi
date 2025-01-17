package com.app.ExamManager.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.ExamManager.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class ServiceToken {
    
    @Value("{api.security.token.secret}")
    private String secret;

    public String generateToken(Usuario user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                            .withIssuer("exam-manager-api")
                            .withSubject(user.getUsername())
                            .withExpiresAt(genExpirationDate())
                            .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error wwhile generating token", exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                        .withIssuer("exam-manager-api")
                        .build()
                        .verify(token)
                        .getSubject();

        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
