package io.github.melquimartins.memora.domain.challenge;

import io.github.melquimartins.memora.domain.alternative.Alternative;
import io.github.melquimartins.memora.domain.alternative.AlternativeRepository;
import io.github.melquimartins.memora.domain.challenge.dto.AnswerChallengeRequest;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeResponse;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeRequest;
import io.github.melquimartins.memora.domain.challenge.mapper.ChallengeMapper;
import io.github.melquimartins.memora.domain.module.Module;
import io.github.melquimartins.memora.domain.module.ModuleRepository;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.shared.exception.BadRequestException;
import io.github.melquimartins.memora.shared.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChallengeService {

    private final ModuleRepository moduleRepository;
    private final AlternativeRepository alternativeRepository;

    private final ChallengeRepository repository;
    private final ChallengeMapper mapper;

    public ChallengeService(
            ModuleRepository moduleRepository,
            AlternativeRepository alternativeRepository,
            ChallengeRepository repository,
            ChallengeMapper mapper
    ) {
        this.moduleRepository = moduleRepository;
        this.alternativeRepository = alternativeRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    public ChallengeResponse create(
            User user,
            Long workspaceId,
            Long moduleId,
            ChallengeRequest request
    ) {
        Module module = moduleRepository
                .findByIdAndWorkspaceIdAndWorkspaceUserId(
                        moduleId,
                        workspaceId,
                        user.getId()
                )
                .orElseThrow(() -> new NotFoundException("Módulo não encontrado."));

        Challenge challenge = new Challenge(request.title());

        challenge.setModule(module);

        repository.save(challenge);

        return mapper.toResponse(challenge);
    }

    public List<ChallengeResponse> getAll(
            User user,
            Long workspaceId,
            Long moduleId
    ) {
        List<Challenge> challenges = repository
                .findAllByModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        moduleId,
                        workspaceId,
                        user.getId()
                )
                .orElseThrow(() -> new NotFoundException("Nenhum desafio foi encontrado."));

        return mapper.toResponseList(challenges);
    }

    public ChallengeResponse get(
            User user,
            Long workspaceId,
            Long moduleId,
            Long challengeId
    ) {
        Challenge challenge = repository
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                )
                .orElseThrow(() -> new NotFoundException("Desafio não encontrado."));

        return mapper.toResponse(challenge);
    }

    public ChallengeResponse update(
            User user,
            Long workspaceId,
            Long moduleId,
            Long challengeId,
            ChallengeRequest request
    ) {
        Challenge challenge = repository
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                )
                .orElseThrow(() -> new NotFoundException("Desafio não encontrado."));

        challenge.setTitle(request.title());

        repository.save(challenge);

        return mapper.toResponse(challenge);
    }

    public void delete(
            User user,
            Long workspaceId,
            Long moduleId,
            Long challengeId
    ) {
        long deleted = repository.deleteByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        );

        if (deleted == 0) {
            throw new NotFoundException("Desafio não encontrado.");
        }
    }

    public void answer(
            User user,
            Long workspaceId,
            Long moduleId,
            Long challengeId,
            AnswerChallengeRequest request
    ) {
        Module module = moduleRepository
                .findByIdAndWorkspaceIdAndWorkspaceUserId(
                        moduleId,
                        workspaceId,
                        user.getId()
                )
                .orElseThrow(() -> new NotFoundException("Módulo não encontrado."));

        Challenge challenge = repository
                .findByIdAndModuleId(
                        challengeId,
                        moduleId
                )
                .orElseThrow(() -> new NotFoundException("Desafio não encontrado."));

        Alternative alternative = alternativeRepository
                .findByIdAndChallengeId(
                        request.alternativeId(),
                        challengeId
                )
                .orElseThrow(() -> new NotFoundException("Alternativa não encontrada."));

        if (!alternative.getCorrect()) {
            throw new BadRequestException("A alternativa selecionada não é a correta.");
        }

        challenge.setAnsweredAt(LocalDateTime.now());

        var difficultyLevel = request
                .difficultyLevel()
                .getDifficultyInterval();

        long amount = ((long) difficultyLevel.amount() *
                challenge.getMultiplier()) *
                module.getMultiplier();
        challenge.setAvailableAgainAt(LocalDateTime.now().plus(amount, difficultyLevel.unit()));

        challenge.setMultiplier(challenge.getMultiplier() + 1);

        repository.save(challenge);
    }

}
