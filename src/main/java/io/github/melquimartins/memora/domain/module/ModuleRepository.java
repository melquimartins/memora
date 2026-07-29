package io.github.melquimartins.memora.domain.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<List<Module>> findAllByWorkspaceIdAndWorkspaceUserId(Long workspaceId, Long userId);

    Optional<Module> findByIdAndWorkspaceIdAndWorkspaceUserId(
            Long moduleId,
            Long workspaceId,
            Long userId
    );

    long deleteByIdAndWorkspaceIdAndWorkspaceUserId(Long moduleId, Long workspaceId, Long userId);
}
