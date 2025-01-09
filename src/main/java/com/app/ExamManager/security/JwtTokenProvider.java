package com.app.ExamManager.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    // Chave secreta utilizada para assinar o token
    @Value("${jwt.secret}")
    private String secretKey;

    // Tempo de expiração do token
    @Value("${jwt.expiration}")
    private long expirationTime;

    // Método para criar o token JWT
    public String createToken(UserDetails userDetails) {
        // Define a data de expiração do token
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        // Cria a chave secreta
        Key key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());

        // Cria o token JWT
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Define o assunto do token (usuário)
                .setIssuedAt(now) // Define a data de emissão
                .setExpiration(expiryDate) // Define a data de expiração
                .signWith(key, SignatureAlgorithm.HS512) // Assina o token com a chave secreta
                .compact();
    }

    // Método para extrair o nome de usuário (subject) do token JWT
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    // Método para obter as claims do token JWT
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()) // A chave secreta é necessária para decifrar o token
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Método para verificar se o token JWT está expirado
    private boolean isTokenExpired(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    // Método para validar o token JWT
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
