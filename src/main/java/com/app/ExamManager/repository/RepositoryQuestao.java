package com.app.ExamManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ExamManager.model.Questao;

public interface RepositoryQuestao extends JpaRepository <Questao, Integer>{
    
}
