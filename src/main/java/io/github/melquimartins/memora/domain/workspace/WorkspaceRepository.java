package io.github.melquimartins.memora.domain.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    List<Workspace> findAllByUserId(Long userId);

    Optional<Workspace> findByIdAndUserId(Long workspaceId, Long userId);

    long deleteByIdAndUserId(Long workspaceId, Long userId);
}
