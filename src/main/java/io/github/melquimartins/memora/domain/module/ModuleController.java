package io.github.melquimartins.memora.domain.module;

import io.github.melquimartins.memora.domain.module.dto.ModuleResponse;
import io.github.melquimartins.memora.shared.dto.ApiResponse;
import io.github.melquimartins.memora.domain.module.dto.CreateModuleRequest;
import io.github.melquimartins.memora.domain.module.dto.UpdateModuleRequest;
import io.github.melquimartins.memora.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/modules")
public class ModuleController {

  private final ModuleService service;

  public ModuleController(ModuleService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ModuleResponse>> create(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId,
        @RequestBody CreateModuleRequest request
  ) {
    ModuleResponse collection = service.create(user, workspaceId, request);

    ApiResponse<ModuleResponse> response = new ApiResponse<>(
          "Módulo criado com sucesso.",
          collection
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ModuleResponse>>> getAll(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId
  ) {
    List<ModuleResponse> collections = service.getAll(user, workspaceId);

    ApiResponse<List<ModuleResponse>> response = new ApiResponse<>(
          "Módulos recuperados com sucesso.",
          collections
    );

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{moduleId}")
  public ResponseEntity<ApiResponse<ModuleResponse>> get(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId,
        @PathVariable Long moduleId
  ) {
    ModuleResponse collection = service.get(user, workspaceId, moduleId);

    ApiResponse<ModuleResponse> response = new ApiResponse<>(
          "Módulo encontrado com sucesso.",
          collection
    );

    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{moduleId}")
  public ResponseEntity<ApiResponse<ModuleResponse>> update(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId,
        @PathVariable Long moduleId,
        @RequestBody UpdateModuleRequest request
  ) {
    ModuleResponse collection = service.update(
          user,
          workspaceId,
          moduleId,
          request
    );

    ApiResponse<ModuleResponse> response = new ApiResponse<>(
          "Módulo atualizado com sucesso.",
          collection
    );

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{moduleId}")
  public ResponseEntity<Void> delete(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId,
        @PathVariable Long moduleId
  ) {
    service.delete(user, workspaceId, moduleId);

    return ResponseEntity.noContent().build();
  }

}
