package com.app.ExamManager.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record ProvaDTO(
    @NotNull(message = "Nome de prova é obrigatório")
    String nome,

    @NotNull(message = "A descrição é obrigatória")
    String descricao,

    @NotNull(message = "A data de criação é obrigatória")
    LocalDate dataCriacao
) {
}
