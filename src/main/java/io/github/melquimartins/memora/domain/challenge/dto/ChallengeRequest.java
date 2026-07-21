package io.github.melquimartins.memora.domain.challenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChallengeRequest(
      @NotBlank(message = "O título é obrigatório.")
      @Size(
            min = 4,
            max = 32,
            message = "O título deve ter entre 4 e 32 caracteres."
      )
      String title
) {
}
