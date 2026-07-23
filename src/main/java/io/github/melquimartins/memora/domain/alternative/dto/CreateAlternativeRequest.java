package io.github.melquimartins.memora.domain.alternative.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAlternativeRequest(
        @NotBlank(message = "O texto é obrigatório.")
        @Size(min = 4, max = 64, message = "O texto deve ter entre 4 e 64 caracteres.")
        String text,

        Boolean correct
) {
    public CreateAlternativeRequest {
        if (correct == null) {
            correct = false;
        }
    }
}
