package com.app.ExamManager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ExamManager.model.Resposta;
import com.app.ExamManager.repository.RepositoryResposta;

@Service
public class ServiceResposta {
    
    @Autowired
    private RepositoryResposta repositoryResposta;

    public Resposta salvarResposta(Resposta resposta){
        
        return repositoryResposta.save(resposta);
    }

    public List<Resposta> buscarTodasRespostas(){

        return repositoryResposta.findAll();
    }

    public Resposta buscarRespostaPorId(int id){

        Optional<Resposta> respostaOptional = repositoryResposta.findById(id);

        if (!respostaOptional.isPresent()) {
            throw new IllegalArgumentException("Resposta não encontrada!");
        }

        return respostaOptional.get();
    }

    public Resposta atualizarResposta(Resposta resposta){

        if (repositoryResposta.existsById(resposta.getId())) {
            
            return repositoryResposta.save(resposta);
        } else {

            throw new IllegalArgumentException("Resposta não encontrada!");
        }
    }

    public void deletarResposta(int id){

        if (repositoryResposta.existsById(id)) {
            
            repositoryResposta.deleteById(id);
        } else {

            throw new IllegalArgumentException("Resposta não encontrada!");
        }
    }
}
