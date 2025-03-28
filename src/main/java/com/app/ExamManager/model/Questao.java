package com.app.ExamManager.model;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.app.ExamManager.DTO.DescricaoDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "questoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Questao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "enunciado", nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @ManyToOne
    @JoinColumn(name = "prova_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Prova prova;

    public Questao(DescricaoDTO dto, Prova prova){
        this.enunciado = dto.texto();
        this.prova = prova;
    }
}
