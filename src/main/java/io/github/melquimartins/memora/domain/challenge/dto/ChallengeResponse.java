package io.github.melquimartins.memora.domain.challenge.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChallengeResponse(
        Long id,
        UUID uuid,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
