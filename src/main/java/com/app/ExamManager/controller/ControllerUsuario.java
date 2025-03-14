package com.app.ExamManager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ExamManager.model.Usuario;
import com.app.ExamManager.service.ServiceUsuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/user")
public class ControllerUsuario {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @GetMapping
    @Operation(summary = "Listar todos usuarios", description = "Retorna todos os usuários cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuários encontradas com sucesso!"),
        @ApiResponse(responseCode = "204", description = "Usuários não encontrados.")
    })
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = serviceUsuario.buscarTodosUsuarios();

        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuário pelo identificador", description = "Retorna usuário correspondente ao identificador parametrizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrada com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable int id){

        try {
            Usuario usuario = serviceUsuario.buscarUsuarioPorId(id);

            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (Exception e) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um usuário", description = "Verifica o cadastro salvo de um usuário e deleta o registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado, erro ao deletar!")
    })
    public ResponseEntity<Void> deletarUsuario(@PathVariable int id) {
        try {

            serviceUsuario.deletarUsuario(id);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}