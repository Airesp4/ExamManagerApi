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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ExamManager.DTO.DescricaoDTO;
import com.app.ExamManager.model.Prova;
import com.app.ExamManager.model.Questao;
import com.app.ExamManager.service.ServiceProva;
import com.app.ExamManager.service.ServiceQuestao;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/questoes")
public class ControllerQuestao {
    
    @Autowired
    private ServiceQuestao serviceQuestao;

    @Autowired
    private ServiceProva serviceProva;

    @PostMapping("/{provaId}")
    @Operation(summary = "Cria uma nova questão", description = "Adiciona uma nova questão com o enunciado fornecido e atribui a mesma à uma prova existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Questão criada com sucesso!"),
        @ApiResponse(responseCode = "204", description = "Questão não pode estar vazia."),
        @ApiResponse(responseCode = "404", description = "Prova não encontrada para registro de questão.")
    })
    public ResponseEntity<Questao> criarQuestao(@PathVariable int provaId, 
                                                 @RequestBody @Valid DescricaoDTO questaoDTO){

        Prova prova = serviceProva.buscarProvaPorId(provaId);

        if (prova == null) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (questaoDTO.texto() == null || questaoDTO.texto().trim().isEmpty()) {
            
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Questao questao = new Questao(questaoDTO, prova);

        serviceQuestao.salvarQuestao(questao);
        return new ResponseEntity<>(questao, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Listar todas questões", description = "Retorna todas as questões cadastradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questões encontradas com sucesso!"),
        @ApiResponse(responseCode = "204", description = "Nenhuma questão cadastrada.")
    })
    public ResponseEntity<List<Questao>> listarQuestoes() {

        List<Questao> questoes = serviceQuestao.buscarTodasQuestoes();

        if (questoes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(questoes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca questão pelo identificador", description = "Retorna questão correspondente ao identificador parametrizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questão encontrada com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Questão não encontrada.")
    })
    public ResponseEntity<Questao> buscarQuestaoPorID(@PathVariable int id){

        try {
            
            Questao questao = serviceQuestao.buscarQuestaoPorId(id);

            return new ResponseEntity<>(questao, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza informações de uma questão", description = "Identifica alterações em um objeto questão cadastrado e atualiza o registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questão atualizada com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Resposta não encontrada para atualização.")
    })
    public ResponseEntity<Questao> atualizarQuestao(@PathVariable int id, @RequestBody @Valid DescricaoDTO questaoDTO) {

        try {
            
            Questao questao = serviceQuestao.buscarQuestaoPorId(id);

            if (!questao.getEnunciado().equals(questaoDTO.texto())) {
                
                questao.setEnunciado(questaoDTO.texto());

                serviceQuestao.atualizarQuestao(questao);
            }

            return new ResponseEntity<>(questao, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma questão", description = "Verifica o cadastro salvo de uma questão e deleta o registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questão deletada com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Questão não encontrada."),
        @ApiResponse(responseCode = "500", description = "Erro de comunicação com servidor.")
    })
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
