package io.github.melquimartins.memora.domain.challenge;

import io.github.melquimartins.memora.domain.challenge.dto.ChallengeResponse;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeRequest;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.shared.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/modules/{moduleId}/challenges")
public class ChallengeController {

    private final ChallengeService service;

    public ChallengeController(ChallengeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChallengeResponse>> create(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @RequestBody ChallengeRequest request
    ) {
        ChallengeResponse challenge = service.create(
              user,
              workspaceId,
              moduleId,
              request
        );

        ApiResponse<ChallengeResponse> response = new ApiResponse<>(
                "Desafio criado com sucesso.",
                challenge
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getAll(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId
    ) {
        List<ChallengeResponse> challenges = service.getAll(
              user,
              workspaceId,
              moduleId
        );

        ApiResponse<List<ChallengeResponse>> response = new ApiResponse<>(
                "Desafios recuperados com sucesso.",
                challenges
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<ApiResponse<ChallengeResponse>> get(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @PathVariable Long challengeId
    ) {
        ChallengeResponse challenge = service.get(
              user,
              workspaceId,
              moduleId,
              challengeId
        );

        ApiResponse<ChallengeResponse> response = new ApiResponse<>(
                "Desafio recuperado com sucesso.",
                challenge
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{challengeId}")
    public ResponseEntity<ApiResponse<ChallengeResponse>> update(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @PathVariable Long challengeId,
            @RequestBody ChallengeRequest request
    ) {
        ChallengeResponse challenge = service.update(
              user,
              workspaceId,
              moduleId,
              challengeId,
              request
        );

        ApiResponse<ChallengeResponse> response = new ApiResponse<>(
                "Desafio atualizado com sucesso.",
                challenge
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{challengeId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @PathVariable Long challengeId
    ) {
        service.delete(
              user,
              workspaceId,
              moduleId,
              challengeId
        );

        return ResponseEntity.noContent().build();
    }

}
