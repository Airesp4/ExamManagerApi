package com.app.ExamManager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.ExamManager.model.Usuario;
import com.app.ExamManager.repository.RepositoryUsuario;
import com.app.ExamManager.service.ServiceUsuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/user")
public class ControllerUsuario {
    
    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @Autowired
    private ServiceUsuario serviceUsuario;

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastra um usuário", description = "Verifica e valida os dados inseridos para cadastro de usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso!"),
        @ApiResponse(responseCode = "400", description = "Dados obrigatórios inválidos ou nulos"),
        @ApiResponse(responseCode = "409", description = "Nome de usuário já cadastrado!")
    })
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario){

        if (usuario.getUsername() == null || usuario.getUsername().isEmpty() ||
            usuario.getPassword() == null || usuario.getPassword().isEmpty() ||
            usuario.getDateBirth() == null) {
                
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (repositoryUsuario.findByUsername(usuario.getUsername()).isPresent()) {
            
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        try {
            Usuario userCriado = new Usuario();
            userCriado.setUsername(usuario.getUsername());
            userCriado.setPassword(usuario.getPassword());
            userCriado.setDateBirth(usuario.getDateBirth());

            serviceUsuario.salvarUsuario(userCriado);

            return new ResponseEntity<>(usuario, HttpStatus.CREATED);
        } catch (Exception e) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Listar todos usuarios", description = "Retorna todos os usuários cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuários encontradas com sucesso!"),
        @ApiResponse(responseCode = "204", description = "Usuários não encontrados.")
    })
    public ResponseEntity<List<Usuario>> listarUsuarios(){

        try {
            List<Usuario> usuarios = serviceUsuario.buscarTodosUsuarios();

            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
           
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Busca usuário pelo identificador", description = "Retorna usuário correspondente ao identificador parametrizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prova encontrada com sucesso!"),
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

    @GetMapping("/atualizar/{id}")
    @Operation(summary = "Atualiza usuário pelo identificador", description = "Permite atualizar o username e/ou a senha de um usuário pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso!"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou nulos."),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
        @ApiResponse(responseCode = "409", description = "Nome de usuário já em uso.")
    })
    public ResponseEntity<Usuario> atualizarUsuario(
            @PathVariable int id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password) {

        try {

            Usuario usuario = serviceUsuario.buscarUsuarioPorId(id);

            if (username != null && !username.isEmpty() && 
                !username.equals(usuario.getUsername()) && 
                repositoryUsuario.findByUsername(username).isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            if (username != null && !username.isEmpty()) {
                usuario.setUsername(username);
            }
            if (password != null && !password.isEmpty()) {
                usuario.setPassword(password);
            }

            serviceUsuario.salvarUsuario(usuario);

            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deleta um usuário", description = "Verifica o cadastro salvo de um usuário e deleta o registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso!"),
        @ApiResponse(responseCode = "204", description = "Usuário não encontrado, erro ao deletar!")
    })
    public ResponseEntity<Void> deletarProva(@PathVariable int id) {
        try {

            serviceUsuario.deletarUsuario(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
