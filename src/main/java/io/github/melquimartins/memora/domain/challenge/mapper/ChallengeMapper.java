package io.github.melquimartins.memora.domain.challenge.mapper;

import io.github.melquimartins.memora.domain.challenge.Challenge;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChallengeMapper {

  public ChallengeResponse toResponse(Challenge challenge) {
    return new ChallengeResponse(
          challenge.getId(),
          challenge.getUuid(),
          challenge.getTitle(),
          challenge.getCreatedAt(),
          challenge.getUpdatedAt()
    );
  }

  public List<ChallengeResponse> toResponseList(List<Challenge> challenges) {
    return challenges.stream().map(this::toResponse).toList();
  }

}
