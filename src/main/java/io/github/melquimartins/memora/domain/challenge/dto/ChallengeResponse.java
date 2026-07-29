package io.github.melquimartins.memora.domain.challenge.dto;

import io.github.melquimartins.memora.domain.alternative.dto.AlternativeResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChallengeResponse(
        Long id,
        UUID uuid,
        String title,
        int multiplier,
        List<AlternativeResponse> alternatives,
        LocalDateTime answeredAt,
        LocalDateTime availableAgainAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
