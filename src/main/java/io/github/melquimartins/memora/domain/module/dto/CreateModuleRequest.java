package io.github.melquimartins.memora.domain.module.dto;

import io.github.melquimartins.memora.shared.validation.annotation.NotBlankIfPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateModuleRequest(
        @NotBlank(message = "O título é obrigatório.")
        @Size(
                min = 4,
                max = 32,
                message = "O título deve ter entre 4 e 32 caracteres."
        )
        String title,

        @Size(
                max = 120,
                message = "A descrição deve ter no máximo 120 caracteres."
        )
        String description
) {
}
