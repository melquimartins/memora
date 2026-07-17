package io.github.melquimartins.memora.domain.workspace;

import io.github.melquimartins.memora.shared.dto.ApiResponse;
import io.github.melquimartins.memora.domain.workspace.dto.CreateWorkspaceRequest;
import io.github.melquimartins.memora.domain.workspace.dto.UpdateWorkspaceRequest;
import io.github.melquimartins.memora.domain.workspace.dto.WorkspaceResponse;
import io.github.melquimartins.memora.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService service;

    public WorkspaceController(WorkspaceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WorkspaceResponse>> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateWorkspaceRequest request
    ) {
        WorkspaceResponse workspace = service.create(user, request);

        ApiResponse<WorkspaceResponse> response = new ApiResponse<>(
                "Área de trabalho criada com sucesso.",
                workspace
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkspaceResponse>>> getAll(
            @AuthenticationPrincipal User user
    ) {
        List<WorkspaceResponse> workspaces = service.getAll(user);

        ApiResponse<List<WorkspaceResponse>> response = new ApiResponse<>(
                "Áreas de trabalho recuperadas com sucesso.",
                workspaces
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> get(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId
    ) {
        WorkspaceResponse workspace = service.get(user, workspaceId);

        ApiResponse<WorkspaceResponse> response = new ApiResponse<>(
                "Área de trabalho recuperada com sucesso.",
                workspace
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> update(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @Valid @RequestBody UpdateWorkspaceRequest request
    ) {
        WorkspaceResponse workspace = service.update(user, workspaceId, request);

        ApiResponse<WorkspaceResponse> response = new ApiResponse<>(
                "Área de trabalho atualizada com sucesso.",
                workspace
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId
    ) {
        service.delete(user, workspaceId);

        return ResponseEntity.noContent().build();
    }

}
