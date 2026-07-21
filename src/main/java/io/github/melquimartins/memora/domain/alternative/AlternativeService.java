package io.github.melquimartins.memora.domain.alternative;

import io.github.melquimartins.memora.domain.alternative.dto.AlternativeResponse;
import io.github.melquimartins.memora.domain.alternative.dto.CreateAlternativeRequest;
import io.github.melquimartins.memora.domain.alternative.dto.UpdateAlternativeRequest;
import io.github.melquimartins.memora.domain.alternative.mapper.AlternativeMapper;
import io.github.melquimartins.memora.domain.challenge.Challenge;
import io.github.melquimartins.memora.domain.challenge.ChallengeRepository;
import io.github.melquimartins.memora.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AlternativeService {

  private final ChallengeRepository challengeRepository;
  private final AlternativeRepository repository;
  private final AlternativeMapper mapper;

  public AlternativeService(
        ChallengeRepository challengeRepository,
        AlternativeRepository repository,
        AlternativeMapper mapper
  ) {
    this.challengeRepository = challengeRepository;
    this.repository = repository;
    this.mapper = mapper;
  }

  public AlternativeResponse create(
        User user,
        Long workspaceId,
        Long moduleId,
        Long challengeId,
        CreateAlternativeRequest request
  ) {
    Challenge challenge = challengeRepository
          .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
          )
          .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Desafio não encontrado."
          ));

    Alternative alternative = new Alternative(
          request.text(),
          request.correct()
    );

    alternative.setChallenge(challenge);

    repository.save(alternative);

    return mapper.toResponse(alternative);
  }

  public List<AlternativeResponse> getAll(
        User user,
        Long workspaceId,
        Long moduleId,
        Long challengeId
  ) {
    challengeRepository
          .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
          );

    List<Alternative> alternatives = repository.findAllByChallengeId(challengeId);

    return mapper.toResponseList(alternatives);
  }

  public AlternativeResponse get(
        User user,
        Long workspaceId,
        Long moduleId,
        Long challengeId,
        Long alternativeId
  ) {
    challengeRepository
          .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
          )
          .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Desafio não encontrado."
          ));

    Alternative alternative = repository
          .findByIdAndChallengeId(alternativeId, challengeId)
          .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Alternativa não encontrada."
          ));

    return mapper.toResponse(alternative);
  }

  public AlternativeResponse update(
        User user,
        Long workspaceId,
        Long moduleId,
        Long challengeId,
        Long alternativeId,
        UpdateAlternativeRequest request)
  {
    challengeRepository
          .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
          )
          .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Desafio não encontrado."
          ));

    Alternative alternative = repository
          .findByIdAndChallengeId(alternativeId, challengeId)
          .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Alternativa não encontrada."
          ));

    if (request.text() != null) {
      alternative.setText(request.text());
    }

    if (request.correct() != null) {
      alternative.setCorrect(request.correct());
    }

    repository.save(alternative);

    return mapper.toResponse(alternative);
  }

  public void delete(
        User user,
        Long workspaceId,
        Long moduleId,
        Long challengeId,
        Long alternativeId
  ) {
    challengeRepository
          .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
          )
          .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Desafio não encontrado."
          ));

    long deleted = repository
          .deleteByIdAndChallengeId(alternativeId, challengeId);

    if (deleted == 0) {
      throw new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Alternativa não encontrada."
      );
    }
  }

}
