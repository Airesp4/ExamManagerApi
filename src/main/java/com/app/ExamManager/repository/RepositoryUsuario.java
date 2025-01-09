package com.app.ExamManager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ExamManager.model.Usuario;

public interface RepositoryUsuario extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
}
