package io.github.melquimartins.memora.domain.challenge;

import io.github.melquimartins.memora.domain.challenge.dto.ChallengeResponse;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeRequest;
import io.github.melquimartins.memora.domain.challenge.mapper.ChallengeMapper;
import io.github.melquimartins.memora.domain.module.Module;
import io.github.melquimartins.memora.domain.module.ModuleRepository;
import io.github.melquimartins.memora.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChallengeService {

    private final ModuleRepository moduleRepository;

    private final ChallengeRepository repository;
    private final ChallengeMapper mapper;

    public ChallengeService(
            ModuleRepository moduleRepository,
            ChallengeRepository repository,
            ChallengeMapper mapper
    ) {
        this.moduleRepository = moduleRepository;
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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Módulo não encontrado."
                ));

        Challenge challenge = new Challenge(
                request.title()
        );

        challenge.setModule(module);

        repository.save(challenge);

        return mapper.toResponse(challenge);
    }

    public List<ChallengeResponse> getAll(
            User user,
            Long workspaceId,
            Long moduleId
    ) {
        moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
                        moduleId,
                        workspaceId,
                        user.getId()
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Módulo não encontrado."
                ));

        List<Challenge> challenges = repository.findAllByModuleId(moduleId);

        return mapper.toResponseList(challenges);
    }

    public ChallengeResponse get(
            User user,
            Long workspaceId,
            Long moduleId,
            Long challengeId
    ) {
        moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
                        moduleId,
                        workspaceId,
                        user.getId()
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Módulo não encontrado."
                ));

        Challenge challenge = repository.findByIdAndModuleId(challengeId, moduleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Desafio não encontrado."
                ));

        return mapper.toResponse(challenge);
    }

    public ChallengeResponse update(
            User user,
            Long workspaceId,
            Long moduleId,
            Long challengeId,
            ChallengeRequest request
    ) {
        moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
                        moduleId,
                        workspaceId,
                        user.getId()
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Módulo não encontrado."
                ));

        Challenge challenge = repository.findByIdAndModuleId(challengeId, moduleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Desafio não encontrado."
                ));

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
        moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
                        moduleId,
                        workspaceId,
                        user.getId()
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Módulo não encontrado."
                ));

        long deleted = repository.deleteByIdAndModuleId(challengeId, moduleId);

        if (deleted == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Desafio não encontrado."
            );
        }
    }

}
