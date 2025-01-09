package com.app.ExamManager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.ExamManager.model.Usuario;
import com.app.ExamManager.repository.RepositoryUsuario;
import com.app.ExamManager.security.JwtTokenProvider;


@Service
public class ServiceUsuario implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Responsável por criar o token JWT

    @Autowired
    private AuthenticationManager authenticationManager; // Gerencia a autenticação

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repositoryUsuario.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles("USER") // Defina o papel conforme necessário
                .build();
    }

    public Usuario salvarUsuario(Usuario usuario){
        // Criptografar a senha do usuário
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        return repositoryUsuario.save(usuario);
    }

    public List<Usuario> buscarTodosUsuarios(){
        return repositoryUsuario.findAll();
    }

    public Usuario buscarUsuarioPorId(int id){
        Optional<Usuario> usuarioOptional = repositoryUsuario.findById(id);

        if (!usuarioOptional.isPresent()) {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }

        return usuarioOptional.get();
    }

    public Usuario atualizarUsuario(Usuario usuario){
        if (repositoryUsuario.existsById(usuario.getId())) {
            return repositoryUsuario.save(usuario);
        } else {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
    }

    public void deletarUsuario(int id){
        if (repositoryUsuario.existsById(id)) {
            repositoryUsuario.deleteById(id);
        } else {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
    }

    // Método que autentica o usuário e retorna o token JWT
    public String autenticarUsuario(String username, String password) {
        try {
            // Autenticação com o AuthenticationManager
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authenticationToken); // Verifica se as credenciais estão corretas
    
            // Caso a autenticação seja bem-sucedida, gera o token JWT
            UserDetails userDetails = loadUserByUsername(username); // Carrega os detalhes do usuário
            return jwtTokenProvider.createToken(userDetails); // Gera o token JWT
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Usuário não encontrado: " + username);
        } catch (Exception e) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }
    }
}
