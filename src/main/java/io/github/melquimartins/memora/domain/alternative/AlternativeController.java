package io.github.melquimartins.memora.domain.alternative;

import io.github.melquimartins.memora.domain.alternative.dto.AlternativeResponse;
import io.github.melquimartins.memora.domain.alternative.dto.CreateAlternativeRequest;
import io.github.melquimartins.memora.domain.alternative.dto.UpdateAlternativeRequest;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.shared.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/modules/{moduleId}/challenges/{challengeId}/alternatives")
public class AlternativeController {

  private final AlternativeService service;

  public AlternativeController(AlternativeService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<AlternativeResponse>> create(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId,
        @PathVariable Long moduleId,
        @PathVariable Long challengeId,
        @RequestBody CreateAlternativeRequest request
  ) {
    AlternativeResponse alternative = service.create(
          user,
          workspaceId,
          moduleId,
          challengeId,
          request
    );

    ApiResponse<AlternativeResponse> response = new ApiResponse<>(
          "Alternativa criada com sucesso.",
          alternative
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<AlternativeResponse>>> getAll(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId,
        @PathVariable Long moduleId,
        @PathVariable Long challengeId
  ) {
    List<AlternativeResponse> alternatives = service.getAll(
          user,
          workspaceId,
          moduleId,
          challengeId
    );

    ApiResponse<List<AlternativeResponse>> response = new ApiResponse<>(
          "Alternativas recuperadas com sucesso.",
          alternatives
    );

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{alternativeId}")
  public ResponseEntity<ApiResponse<AlternativeResponse>> get(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId,
        @PathVariable Long moduleId,
        @PathVariable Long challengeId,
        @PathVariable Long alternativeId
  ) {
    AlternativeResponse alternative = service.get(
          user,
          workspaceId,
          moduleId,
          challengeId,
          alternativeId
    );

    ApiResponse<AlternativeResponse> response = new ApiResponse<>(
          "Alternativa encontrada com sucesso.",
          alternative
    );

    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{alternativeId}")
  public ResponseEntity<ApiResponse<AlternativeResponse>> update(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId,
        @PathVariable Long moduleId,
        @PathVariable Long challengeId,
        @PathVariable Long alternativeId,
        @RequestBody UpdateAlternativeRequest request
  ) {
    AlternativeResponse alternative = service.update(
          user,
          workspaceId,
          moduleId,
          challengeId,
          alternativeId,
          request
    );

    ApiResponse<AlternativeResponse> response = new ApiResponse<>(
          "Alternativa atualizada com sucesso.",
          alternative
    );

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{alternativeId}")
  public ResponseEntity<Void> delete(
        @AuthenticationPrincipal User user,
        @PathVariable Long workspaceId,
        @PathVariable Long moduleId,
        @PathVariable Long challengeId,
        @PathVariable Long alternativeId
  ) {
    service.delete(
          user,
          workspaceId,
          moduleId,
          challengeId,
          alternativeId
    );

    return ResponseEntity.noContent().build();
  }

}
