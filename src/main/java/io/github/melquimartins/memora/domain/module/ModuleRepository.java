package io.github.melquimartins.memora.domain.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
  List<Module> findAllByWorkspaceId(Long workspaceId);

  Optional<Module> findByIdAndWorkspaceId(
        Long collectionId,
        Long workspaceId
  );

  Optional<Module> findByIdAndWorkspaceIdAndWorkspaceUserId(
        Long collectionId,
        Long workspaceId,
        Long userId
  );

  long deleteByIdAndWorkspaceId(Long collectionId, Long workspaceId);
}
