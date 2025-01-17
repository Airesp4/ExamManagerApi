package com.app.ExamManager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.ExamManager.model.Usuario;
import com.app.ExamManager.repository.RepositoryUsuario;


@Service
public class ServiceUsuario {

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        return repositoryUsuario.save(usuario);
    }

    public List<Usuario> buscarTodosUsuarios() {
        return repositoryUsuario.findAll();
    }

    public Usuario buscarUsuarioPorId(int id) {
        return repositoryUsuario.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
    }

    public Usuario atualizarUsuario(Usuario usuario) {
        if (repositoryUsuario.existsById(usuario.getId())) {
            return repositoryUsuario.save(usuario);
        } else {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
    }

    public void deletarUsuario(int id) {
        if (repositoryUsuario.existsById(id)) {
            repositoryUsuario.deleteById(id);
        } else {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
    }
}