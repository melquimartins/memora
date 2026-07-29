package io.github.melquimartins.memora.domain.alternative;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlternativeRepository extends JpaRepository<Alternative, Long> {
    Optional<List<Alternative>> findAllByChallengeIdAndChallengeModuleIdAndChallengeModuleWorkspaceIdAndChallengeModuleWorkspaceUserId(
            Long challengeId,
            Long moduleId,
            Long workspaceId,
            Long userId
    );

    Optional<Alternative> findByIdAndChallengeIdAndChallengeModuleIdAndChallengeModuleWorkspaceIdAndChallengeModuleWorkspaceUserId(
            Long alternativeId,
            Long challengeId,
            Long moduleId,
            Long workspaceId,
            Long userId
    );

    Optional<Alternative> findByIdAndChallengeId(Long alternativeId, Long challengeId);

    long deleteByIdAndChallengeIdAndChallengeModuleIdAndChallengeModuleWorkspaceIdAndChallengeModuleWorkspaceUserId(
            Long alternativeId,
            Long challengeId,
            Long moduleId,
            Long workspaceId,
            Long userId
    );
}
