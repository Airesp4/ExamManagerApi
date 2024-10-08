package com.app.ExamManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ExamManager.model.Resposta;

public interface RepositoryResposta extends JpaRepository <Resposta, Integer>{
    
}
