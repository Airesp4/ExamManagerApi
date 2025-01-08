package com.app.ExamManager.controller;

import java.util.List;
import java.time.LocalDate;

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
import com.app.ExamManager.service.ServiceProva;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/provas")
public class ControllerProva {
    
    @Autowired
    private ServiceProva serviceProva;

    @PostMapping("/cadastro")
    @Operation(summary = "Cria uma nova prova", description = "Adiciona uma nova prova com o nome fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Prova criada com sucesso!"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "422", description = "Erro ao criar prova, nome inválido!")
    })
    public ResponseEntity<Prova> criarProva(@RequestParam String nome, @RequestParam String descricao){

        if (nome == null || nome.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(null);
        }

        try {
            Prova prova = new Prova();
            prova.setNome(nome);
            prova.setDescricao(descricao);
            prova.setDataCriacao(LocalDate.now());

            serviceProva.salvarProva(prova);

            return new ResponseEntity<>(prova, HttpStatus.CREATED);
        } catch (Exception e) {
            
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Listar todas provas", description = "Retorna todas as provas cadastradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Provas encontradas com sucesso!")
    })
    public ResponseEntity<List<Prova>> listarProvas() {

        try {
            
            List<Prova> provas = serviceProva.buscarTodasProvas();
        
            return new ResponseEntity<>(provas, HttpStatus.OK);

        } catch (Exception e) {
            
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Busca prova pelo identificador", description = "Retorna prova correspondente ao identificador parametrizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prova encontrada com sucesso!")
    })
    public ResponseEntity<Prova> buscarProvaPorId(@PathVariable int id) {

        try {

            Prova prova = serviceProva.buscarProvaPorId(id);
            
            return new ResponseEntity<>(prova, HttpStatus.OK);
    
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualiza informações de uma prova", description = "Identifica alterações em um objeto prova cadastrado e atualiza o registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prova atualizada com sucesso!"),
        @ApiResponse(responseCode = "400", description = "Erro. Registro de prova não alterado! Parâmetros inválidos."),
        @ApiResponse(responseCode = "404", description = "Erro. Id incorreto ou inexistente, prova não encontrada!")
    })
    public ResponseEntity<Prova> atualizarProva(@PathVariable int id, 
                                            @RequestParam String nome, 
                                            @RequestParam String descricao) {

        if (nome == null || descricao == null || nome.isEmpty() || descricao.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Prova prova = serviceProva.buscarProvaPorId(id);

            if (prova != null && (!prova.getNome().equals(nome) || !prova.getDescricao().equals(descricao))) {
                prova.setNome(nome);
                prova.setDescricao(descricao);

                serviceProva.atualizarProva(prova);
                return new ResponseEntity<>(prova, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deleta uma prova", description = "Verifica o cadastro salvo de uma prova e deleta o registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prova deletada com sucesso!")
    })
    public ResponseEntity<Void> deletarProva(@PathVariable int id) {
        try {

            serviceProva.deletarProva(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
