package io.github.melquimartins.memora.domain.workspace.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkspaceResponse(
        Long id,
        UUID uuid,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}