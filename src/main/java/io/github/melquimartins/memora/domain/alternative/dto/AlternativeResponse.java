package io.github.melquimartins.memora.domain.alternative.dto;

import java.time.LocalDateTime;

public record AlternativeResponse(
      Long id,
      String text,
      Boolean correct,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {
}
