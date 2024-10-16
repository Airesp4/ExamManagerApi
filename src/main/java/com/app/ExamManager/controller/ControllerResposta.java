package com.app.ExamManager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.ExamManager.model.Questao;
import com.app.ExamManager.model.Resposta;
import com.app.ExamManager.service.ServiceQuestao;
import com.app.ExamManager.service.ServiceResposta;

@RestController
@RequestMapping("/respostas")
public class ControllerResposta {
    
    @Autowired
    private ServiceResposta serviceResposta;

    @Autowired
    private ServiceQuestao serviceQuestao;

    @PostMapping("/cadastro")
    public ResponseEntity<Resposta> criarResposta(@RequestParam int questaoId, @RequestParam String descricao) {

        if (descricao == null || descricao.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Questao questao = serviceQuestao.buscarQuestaoPorId(questaoId);
        if (questao == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resposta resposta = new Resposta();
        resposta.setQuestaoId(questaoId);
        resposta.setDescricao(descricao);

        resposta = serviceResposta.salvarResposta(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Resposta>> listarRespostas(){

        List<Resposta> respostas = serviceResposta.buscarTodasRespostas();

        if (respostas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(respostas, HttpStatus.OK);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Resposta> buscarRespostaPorId(@PathVariable int id){

        try {
            
            Resposta resposta = serviceResposta.buscarRespostaPorId(id);

            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Resposta> atualizarResposta(@PathVariable int id, @RequestParam String descricao){
        
        try {
            Resposta resposta = serviceResposta.buscarRespostaPorId(id);
    
            if (resposta == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    
            if (!resposta.getDescricao().equals(descricao)) {

                resposta.setDescricao(descricao);

                serviceResposta.atualizarResposta(resposta);
                return new ResponseEntity<>(resposta, HttpStatus.OK);
            } else {

                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarResposta(@PathVariable int id) {

        try {

            serviceResposta.deletarResposta(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
