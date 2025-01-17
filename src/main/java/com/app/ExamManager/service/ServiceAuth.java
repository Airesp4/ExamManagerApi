package com.app.ExamManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.ExamManager.repository.RepositoryUsuario;

@Service
public class ServiceAuth implements UserDetailsService{

    @Autowired
    RepositoryUsuario repositoryUsuario;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        return repositoryUsuario.findByUsername(username);
    }
    
}
