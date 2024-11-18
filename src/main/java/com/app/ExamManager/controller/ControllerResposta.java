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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/respostas")
public class ControllerResposta {
    
    @Autowired
    private ServiceResposta serviceResposta;

    @Autowired
    private ServiceQuestao serviceQuestao;

    @PostMapping("/cadastro/{questaoId}")
    @Operation(summary = "Cria uma nova resposta", description = "Adiciona uma nova resposta para uma questão existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resposta criada com sucesso!"),
    })
    public ResponseEntity<Resposta> criarResposta(@RequestParam int questaoId, @RequestParam String descricao) {

        if (descricao == null || descricao.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Questao questao = serviceQuestao.buscarQuestaoPorId(questaoId);
        if (questao == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resposta resposta = new Resposta();
        resposta.setDescricao(descricao);
        resposta.setQuestao(questao);

        resposta = serviceResposta.salvarResposta(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Listar todas respostas", description = "Retorna todas as respostas cadastradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respostas encontradas com sucesso!")
    })
    public ResponseEntity<List<Resposta>> listarRespostas(){

        List<Resposta> respostas = serviceResposta.buscarTodasRespostas();

        if (respostas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(respostas, HttpStatus.OK);
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Busca resposta pelo identificador", description = "Retorna resposta correspondente ao identificador parametrizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resposta encontrada com sucesso!")
    })
    public ResponseEntity<Resposta> buscarRespostaPorId(@PathVariable int id){

        try {
            
            Resposta resposta = serviceResposta.buscarRespostaPorId(id);

            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualiza resposta cadastrada", description = "Altera o registro de uma resposta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resposta atualizada com sucesso!")
    })
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
    @Operation(summary = "Deleta uma resposta", description = "Verifica o cadastro salvo de uma resposta e deleta o registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resposta deletada com sucesso!")
    })
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
