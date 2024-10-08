package com.app.ExamManager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ExamManager.model.Prova;
import com.app.ExamManager.repository.RepositoryProva;

@Service
public class ServiceProva {
    
    @Autowired
    private RepositoryProva repositoryProva;


    public Prova salvarProva(Prova prova){

        return repositoryProva.save(prova);
    }

    public List<Prova> buscarTodasProvas(){
        
        return repositoryProva.findAll();
    }

    public Prova buscarProvaPorId(int id) {

        Optional<Prova> provaOptional = repositoryProva.findById(id);

        if (!provaOptional.isPresent()) {
            throw new IllegalArgumentException("Prova não encontrada!");
        }
    
        return provaOptional.get();
    }

    public Prova atualizarProva(Prova prova) {
        
        if (repositoryProva.existsById(prova.getId())) {

            return repositoryProva.save(prova);
        } else {

            throw new IllegalArgumentException("Prova não encontrada!");
        }
    }

    public void deletarProva(int id) {

        if (repositoryProva.existsById(id)) {
            
            repositoryProva.deleteById(id);
        } else {
            throw new IllegalArgumentException("Prova não encontrada!");
        }
    }
}
