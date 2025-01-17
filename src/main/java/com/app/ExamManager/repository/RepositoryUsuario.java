package com.app.ExamManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.ExamManager.model.Usuario;

public interface RepositoryUsuario extends JpaRepository<Usuario, Integer> {
    UserDetails findByUsername(String username);
}
