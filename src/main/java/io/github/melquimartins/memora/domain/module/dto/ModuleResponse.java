package io.github.melquimartins.memora.domain.module.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ModuleResponse(
        Long id,
        UUID uuid,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
