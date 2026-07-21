package io.github.melquimartins.memora.domain.auth.dto;

import jakarta.validation.constraints.*;

public record SignInRequest(
      @NotBlank(message = "O e-mail é obrigatório.")
      @Email(message = "Por favor, informe um e-mail válido.")
      String email,

      @NotBlank(message = "A senha é obrigatória.")
      @Size(
            min = 8,
            max = 64,
            message = "A senha deve ter entre 8 e 64 caracteres."
      )
      String password
) {
}
