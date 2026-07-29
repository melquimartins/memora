package io.github.melquimartins.memora.domain.module;

import io.github.melquimartins.memora.domain.module.dto.ModuleResponse;
import io.github.melquimartins.memora.domain.module.dto.CreateModuleRequest;
import io.github.melquimartins.memora.domain.module.dto.UpdateModuleRequest;
import io.github.melquimartins.memora.domain.module.mapper.ModuleMapper;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.domain.workspace.Workspace;
import io.github.melquimartins.memora.domain.workspace.WorkspaceRepository;
import io.github.melquimartins.memora.shared.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ModuleService {

    private final WorkspaceRepository workspaceRepository;

    private final ModuleRepository repository;
    private final ModuleMapper mapper;

    public ModuleService(
            WorkspaceRepository workspaceRepository,
            ModuleRepository repository,
            ModuleMapper mapper
    ) {
        this.workspaceRepository = workspaceRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    public ModuleResponse create(User user, Long workspaceId, CreateModuleRequest request) {
        Workspace workspace = workspaceRepository.findByIdAndUserId(workspaceId, user.getId())
                .orElseThrow(() -> new NotFoundException("Área de trabalho não encontrada."));

        Module module = new Module(
                request.title(),
                request.description()
        );

        module.setWorkspace(workspace);

        repository.save(module);

        return mapper.toResponse(module);
    }

    public List<ModuleResponse> getAll(
            User user,
            Long workspaceId
    ) {
        List<Module> modules = repository
                .findAllByWorkspaceIdAndWorkspaceUserId(workspaceId, user.getId())
                .orElseThrow(() -> new NotFoundException("Nenhum módulo foi encontrado."));

        return mapper.toResponseList(modules);
    }

    public ModuleResponse get(User user, Long workspaceId, Long moduleId) {
        Module module = repository
                .findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId())
                .orElseThrow(() -> new NotFoundException("Módulo não encontrado."));

        return mapper.toResponse(module);
    }

    public ModuleResponse update(
            User user,
            Long workspaceId,
            Long moduleId,
            UpdateModuleRequest request
    ) {
        Module module = repository
                .findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId())
                .orElseThrow(() -> new NotFoundException("Módulo não encontrado."));

        if (request.title() != null) {
            module.setTitle(request.title());
        }

        if (request.description() != null) {
            module.setDescription(request.description());
        }

        repository.save(module);

        return mapper.toResponse(module);
    }

    public void delete(User user, Long workspaceId, Long moduleId) {
        long deleted = repository.deleteByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
        );

        if (deleted == 0) {
            throw new NotFoundException("Módulo não encontrado.");
        }
    }

}
