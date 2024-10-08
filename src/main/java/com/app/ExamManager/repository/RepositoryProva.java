package com.app.ExamManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ExamManager.model.Prova;

@Repository
public interface RepositoryProva extends JpaRepository<Prova, Integer>{
    
}
