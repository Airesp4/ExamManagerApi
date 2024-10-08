package com.app.ExamManager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ExamManager.model.Questao;
import com.app.ExamManager.repository.RepositoryQuestao;

@Service
public class ServiceQuestao {
    
    @Autowired
    private RepositoryQuestao repositoryQuestao;

    public Questao salvarQuestao(Questao questao){
        
        return repositoryQuestao.save(questao);
    }

    public List<Questao> buscarTodasQuestoes(){
        
        return repositoryQuestao.findAll();
    }

    public Questao buscarQuestaoPorId(int id) {

        Optional<Questao> questaoOptional = repositoryQuestao.findById(id);

        if (!questaoOptional.isPresent()) {
            throw new IllegalArgumentException("Questão não encontrada!");
        }
    
        return questaoOptional.get();
    }

    public Questao atualizarQuestao(Questao questao) {

        if (repositoryQuestao.existsById(questao.getId())) {

            return repositoryQuestao.save(questao);
        } else {

            throw new IllegalArgumentException("Questão não encontrada!");
        }
    }

    public void deletarQuestao(int id) {

        if (repositoryQuestao.existsById(id)) {
            
            repositoryQuestao.deleteById(id);
        } else {

            throw new IllegalArgumentException("Questão não encontrada!");
        }
    }
}
