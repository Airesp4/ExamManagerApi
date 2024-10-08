package com.app.ExamManager.controller;

import java.util.List;
import java.time.LocalDateTime;

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

@RestController
@RequestMapping("/provas")
public class ControllerProva {
    
    @Autowired
    private ServiceProva serviceProva;

    @PostMapping("/cadastro")
    public ResponseEntity<Prova> criarProva(@RequestParam String nome){

        Prova prova = new Prova();
        prova.setNome(nome);
        prova.setDataCriacao(LocalDateTime.now());

        try {
            prova = serviceProva.salvarProva(prova);

            return new ResponseEntity<>(prova, HttpStatus.CREATED);
        } catch (Exception e) {
            
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Prova>> listarProvas() {

        try {
            
            List<Prova> provas = serviceProva.buscarTodasProvas();
        
            return new ResponseEntity<>(provas, HttpStatus.OK);

        } catch (Exception e) {
            
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Prova> buscarProvaPorId(@PathVariable int id) {

        try {

            Prova prova = serviceProva.buscarProvaPorId(id);
            
            return new ResponseEntity<>(prova, HttpStatus.OK);
    
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Prova> atualizarProva(@PathVariable int id, @RequestParam String nome) {

        try {
            
            Prova prova = serviceProva.buscarProvaPorId(id);

            if (!prova.getNome().equals(nome)) {
                prova.setNome(nome);

                serviceProva.atualizarProva(prova);
            }

            return new ResponseEntity<>(prova, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
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
