package io.github.melquimartins.memora.domain.challenge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findAllByModuleId(Long moduleId);

    Optional<Challenge> findByIdAndModuleId(Long challengeId, Long moduleId);

    Optional<Challenge> findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
          Long challengeId,
          Long moduleId,
          Long workspaceId,
          Long userId
    );

    long deleteByIdAndModuleId(Long challengeId, Long collectionId);
}
