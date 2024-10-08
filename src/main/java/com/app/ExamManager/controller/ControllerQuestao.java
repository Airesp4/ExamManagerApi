package com.app.ExamManager.controller;

import java.util.ArrayList;
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

import com.app.ExamManager.model.Prova;
import com.app.ExamManager.model.Questao;
import com.app.ExamManager.model.Resposta;
import com.app.ExamManager.service.ServiceProva;
import com.app.ExamManager.service.ServiceQuestao;

@RestController
@RequestMapping("/questoes")
public class ControllerQuestao {
    
    @Autowired
    private ServiceQuestao serviceQuestao;

    @Autowired
    private ServiceProva serviceProva;

    @PostMapping("/cadastro/{provaId}")
    public ResponseEntity<Questao> criarQuestao(@PathVariable int provaId, 
                                                @RequestParam String enunciado, 
                                                @RequestParam List<String> opcoesResposta){

        Prova prova = serviceProva.buscarProvaPorId(provaId);

        if (prova == null) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (enunciado == null & enunciado.trim().isEmpty()) {
            
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Questao questao = new Questao();
        questao.setEnunciado(enunciado);
        
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Questao>> listarQuestoes() {

        List<Questao> questoes = serviceQuestao.buscarTodasQuestoes();

        if (questoes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(questoes, HttpStatus.OK);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Questao> buscarQuestaoPorID(@PathVariable int id){

        try {
            
            Questao questao = serviceQuestao.buscarQuestaoPorId(id);

            return new ResponseEntity<>(questao, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Questao> atualizarQuestao(@PathVariable int id, @RequestParam String enunciado) {

        try {
            
            Questao questao = serviceQuestao.buscarQuestaoPorId(id);

            if (!questao.getEnunciado().equals(enunciado)) {
                
                questao.setEnunciado(enunciado);

                serviceQuestao.atualizarQuestao(questao);
            }

            return new ResponseEntity<>(questao, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deletarQuestao(@PathVariable int id) {

        try {

            serviceQuestao.deletarQuestao(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
