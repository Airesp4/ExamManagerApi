package com.app.ExamManager.DTO;

import jakarta.validation.constraints.NotEmpty;

public record DescricaoDTO(
    @NotEmpty(message = "Enunciado não pode ser vazio")
    String texto
) {}
