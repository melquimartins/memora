package io.github.melquimartins.memora.domain.workspace.mapper;

import io.github.melquimartins.memora.domain.workspace.Workspace;
import io.github.melquimartins.memora.domain.workspace.dto.WorkspaceResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkspaceMapper {

  public WorkspaceResponse toResponse(Workspace workspace) {
    return new WorkspaceResponse(
          workspace.getId(),
          workspace.getUuid(),
          workspace.getTitle(),
          workspace.getDescription(),
          workspace.getCreatedAt(),
          workspace.getUpdatedAt()
    );
  }

  public List<WorkspaceResponse> toResponseList(List<Workspace> workspaces) {
    return workspaces.stream().map(this::toResponse).toList();
  }

}
