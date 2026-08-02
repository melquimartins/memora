package io.github.melquimartins.memora.domain.workspace;

import io.github.melquimartins.memora.domain.workspace.dto.CreateWorkspaceRequest;
import io.github.melquimartins.memora.domain.workspace.dto.UpdateWorkspaceRequest;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.domain.workspace.dto.WorkspaceResponse;
import io.github.melquimartins.memora.domain.workspace.mapper.WorkspaceMapper;
import io.github.melquimartins.memora.shared.exception.BadRequestException;
import io.github.melquimartins.memora.shared.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WorkspaceService {

    private final WorkspaceRepository repository;
    private final WorkspaceMapper mapper;

    public WorkspaceService(
            WorkspaceRepository repository,
            WorkspaceMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public WorkspaceResponse create(User user, CreateWorkspaceRequest request) {
        Workspace workspace = new Workspace(request.title(), request.description());
        workspace.setUser(user);

        repository.save(workspace);

        return mapper.toResponse(workspace);
    }

    public List<WorkspaceResponse> getAll(User user) {
        List<Workspace> workspaces = repository.findAllByUserId(user.getId());

        if (workspaces.isEmpty()) {
            throw new NotFoundException("Nenhuma área de trabalho encontrada.");
        }

        return mapper.toResponseList(workspaces);
    }

    public WorkspaceResponse get(User user, Long workspaceId) {
        Workspace workspace = repository.findByIdAndUserId(workspaceId, user.getId())
                .orElseThrow(() -> new NotFoundException("Área de trabalho não encontrada."));

        return mapper.toResponse(workspace);
    }

    public WorkspaceResponse update(User user, Long workspaceId, UpdateWorkspaceRequest request) {
        Workspace workspace = repository
                .findByIdAndUserId(workspaceId, user.getId())
                .orElseThrow(() -> new NotFoundException("Área de trabalho não encontrada."));

        if (request.title() == null && request.description() == null) {
            throw new BadRequestException("Nenhum campo para atualizar foi informado.");
        }

        if (request.title() != null) {
            workspace.setTitle(request.title());
        }

        if (request.description() != null) {
            workspace.setDescription(request.description());
        }

        repository.save(workspace);

        return mapper.toResponse(workspace);
    }

    public void delete(User user, Long workspaceId) {
        long deleted = repository.deleteByIdAndUserId(workspaceId, user.getId());

        if (deleted == 0) {
            throw new NotFoundException("Área de trabalho não encontrada.");
        }
    }

}
