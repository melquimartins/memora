package io.github.melquimartins.memora.domain.module;

import io.github.melquimartins.memora.domain.module.dto.ModuleResponse;
import io.github.melquimartins.memora.domain.module.dto.CreateModuleRequest;
import io.github.melquimartins.memora.domain.module.dto.UpdateModuleRequest;
import io.github.melquimartins.memora.domain.module.mapper.ModuleMapper;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.domain.workspace.Workspace;
import io.github.melquimartins.memora.domain.workspace.WorkspaceRepository;
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

    public ModuleResponse create(
          User user,
          Long workspaceId,
          CreateModuleRequest request
    ) {
        Workspace workspace = workspaceRepository.findByIdAndUserId(workspaceId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Área de trabalho não encontrada."
                ));

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
        workspaceRepository.findByIdAndUserId(workspaceId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Área de trabalho não encontrada."
                ));

        List<Module> modules = repository.findAllByWorkspaceId(workspaceId);

        return mapper.toResponseList(modules);
    }

    public ModuleResponse get(
          User user,
          Long workspaceId,
          Long collectionId
    ) {
        workspaceRepository.findByIdAndUserId(workspaceId, user.getId())
              .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Área de trabalho não encontrada."
              ));

        Module module = repository.findByIdAndWorkspaceId(
              collectionId,
              workspaceId
        ).orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Módulo não encontrado."
        ));

        return mapper.toResponse(module);
    }

    public ModuleResponse update(
            User user,
            Long workspaceId,
            Long collectionId,
            UpdateModuleRequest request
    ) {
        workspaceRepository.findByIdAndUserId(workspaceId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Área de trabalho não encontrada."
                ));

        Module module = repository
              .findByIdAndWorkspaceId(collectionId, workspaceId)
              .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Módulo não encontrado."
              ));

        if (request.title() != null) {
            module.setTitle(request.title());
        }

        if (request.description() != null) {
            module.setDescription(request.description());
        }

        repository.save(module);

        return mapper.toResponse(module);
    }

    public void delete(
          User user,
          Long workspaceId,
          Long collectionId
    ) {
        workspaceRepository.findByIdAndUserId(workspaceId, user.getId())
              .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Área de trabalho não encontrada."
              ));

        long deleted = repository
              .deleteByIdAndWorkspaceId(collectionId, workspaceId);

        if (deleted == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Módulo não encontrado."
            );
        }
    }

}
