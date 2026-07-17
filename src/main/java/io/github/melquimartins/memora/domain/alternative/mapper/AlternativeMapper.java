package io.github.melquimartins.memora.domain.alternative.mapper;

import io.github.melquimartins.memora.domain.alternative.Alternative;
import io.github.melquimartins.memora.domain.alternative.dto.AlternativeResponse;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlternativeMapper {

  public AlternativeResponse toResponse(Alternative alternative) {
    return new AlternativeResponse(
          alternative.getId(),
          alternative.getText(),
          alternative.getCorrect(),
          alternative.getCreatedAt(),
          alternative.getUpdatedAt()
    );
  }

  public List<AlternativeResponse> toResponseList(List<Alternative> alternatives) {
    return alternatives.stream().map(this::toResponse).toList();
  }

}
