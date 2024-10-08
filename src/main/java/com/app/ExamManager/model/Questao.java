package com.app.ExamManager.model;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "questoes")
public class Questao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "enunciado", nullable = false, length = 500)
    private String enunciado;

    @ManyToOne
    @JoinColumn(name = "prova_id", nullable = false)
    private Prova prova;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL)
    private List<Resposta> opcoesResposta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Prova getProva() {
        return prova;
    }

    public void setProva(Prova prova) {
        this.prova = prova;
    }

    public List<Resposta> getOpcoesResposta() {
        return opcoesResposta;
    }

    public void setOpcoesResposta(List<Resposta> opcoesResposta) {
        this.opcoesResposta = opcoesResposta;
    }
}
