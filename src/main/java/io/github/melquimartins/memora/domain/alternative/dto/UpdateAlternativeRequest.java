package io.github.melquimartins.memora.domain.alternative.dto;

import io.github.melquimartins.memora.shared.validation.annotation.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

public record UpdateAlternativeRequest(
        @NotBlankIfPresent
        @Size(min = 4, max = 64, message = "O texto deve ter entre 4 e 64 caracteres.")
        String text,

        Boolean correct
) {
}