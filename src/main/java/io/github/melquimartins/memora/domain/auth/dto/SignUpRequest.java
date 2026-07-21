package io.github.melquimartins.memora.domain.auth.dto;

import io.github.melquimartins.memora.shared.validation.annotation.ValidFullName;
import io.github.melquimartins.memora.shared.validation.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
      @NotBlank(message = "O nome é obrigatório.")
      @ValidFullName
      String name,

      @NotBlank(message = "O e-mail é obrigatório.")
      @Email(message = "Por favor, informe um e-mail válido.")
      String email,

      @NotBlank(message = "A senha é obrigatória.")
      @Size(
            min = 8,
            max = 64,
            message = "A senha deve ter entre 8 e 64 caracteres."
      )
      @ValidPassword
      String password
) {
}
