package io.github.melquimartins.memora.domain.challenge.dto;

import io.github.melquimartins.memora.domain.challenge.enums.DifficultyLevel;
import jakarta.validation.constraints.NotNull;

public record AnswerChallengeRequest(
        @NotNull(message = "A alternativa é obrigatória.")
        Long alternativeId,

        @NotNull(message = "O nível de dificuldade é obrigatório.")
        DifficultyLevel difficultyLevel
) {
}
