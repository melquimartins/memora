package io.github.melquimartins.memora.domain.challenge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<List<Challenge>> findAllByModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
            Long moduleId,
            Long workspaceId,
            Long userId
    );

    Optional<Challenge> findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
            Long challengeId,
            Long moduleId,
            Long workspaceId,
            Long userId
    );

    Optional<Challenge> findByIdAndModuleId(Long challengeId, Long moduleId);

    long deleteByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
            Long challengeId,
            Long moduleId,
            Long workspaceId,
            Long userId
    );
}
