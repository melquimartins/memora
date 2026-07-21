package io.github.melquimartins.memora.domain.alternative;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlternativeRepository extends JpaRepository<Alternative, Long> {
  List<Alternative> findAllByChallengeId(Long challengeId);

  Optional<Alternative> findByIdAndChallengeId(
        Long alternativeId,
        Long challengeId
  );

  long deleteByIdAndChallengeId(Long alternativeId, Long challengeId);
}
