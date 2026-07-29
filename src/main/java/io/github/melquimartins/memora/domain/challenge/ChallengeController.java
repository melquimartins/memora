package io.github.melquimartins.memora.domain.challenge;

import io.github.melquimartins.memora.domain.challenge.dto.AnswerChallengeRequest;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeRequest;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeResponse;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.shared.dto.ResponseEnvelope;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Desafio",
        description = "Endpoints de gerenciamento de desafios"
)
@RestController
@RequestMapping("/api/workspaces/{workspaceId}/modules/{moduleId}/challenges")
public class ChallengeController {

    private final ChallengeService service;

    public ChallengeController(ChallengeService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cria um novo desafio",
            description = "Cria um desafio a partir dos dados informados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Desafio criado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de requisição inválidos."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Módulo não encontrado."
            )
    })
    @PostMapping
    public ResponseEntity<ResponseEnvelope<ChallengeResponse>> create(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @Valid @RequestBody ChallengeRequest request
    ) {
        ChallengeResponse challenge = service.create(
                user,
                workspaceId,
                moduleId,
                request
        );

        ResponseEnvelope<ChallengeResponse> response = new ResponseEnvelope<>(
                "Desafio criado com sucesso.",
                challenge
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Busca todos os desafios",
            description = "Busca todos os desafios disponíveis para o módulo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Desafios recuperados com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum desafio encontrado."
            )
    })
    @GetMapping
    public ResponseEntity<ResponseEnvelope<List<ChallengeResponse>>> getAll(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId
    ) {
        List<ChallengeResponse> challenges = service.getAll(
                user,
                workspaceId,
                moduleId
        );

        ResponseEnvelope<List<ChallengeResponse>> response = new ResponseEnvelope<>(
                "Desafios recuperados com sucesso.",
                challenges
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Busca um desafio",
            description = "Busca um desafio específico pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Desafio recuperado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum desafio encontrado."
            )
    })
    @GetMapping("/{challengeId}")
    public ResponseEntity<ResponseEnvelope<ChallengeResponse>> get(
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

        ResponseEnvelope<ChallengeResponse> response = new ResponseEnvelope<>(
                "Desafio recuperado com sucesso.",
                challenge
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualiza informações de um desafio",
            description = "Atualiza informações de um desafio pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Desafio atualizado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Desafio não encontrado."
            )
    })
    @PatchMapping("/{challengeId}")
    public ResponseEntity<ResponseEnvelope<ChallengeResponse>> update(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @PathVariable Long challengeId,
            @Valid @RequestBody ChallengeRequest request
    ) {
        ChallengeResponse challenge = service.update(
                user,
                workspaceId,
                moduleId,
                challengeId,
                request
        );

        ResponseEnvelope<ChallengeResponse> response = new ResponseEnvelope<>(
                "Desafio atualizado com sucesso.",
                challenge
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Deleta um desafio",
            description = "Deleta um desafio pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Desafio deletado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Desafio não encontrado."
            )
    })
    @DeleteMapping("/{challengeId}")
    public ResponseEntity<String> delete(
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

        return ResponseEntity.ok().body("Desafio deletado com sucesso.");
    }

    @Operation(
            summary = "Responde um desafio",
            description = "Responde um desafio pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Desafio respondido com sucesso."
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "A alternativa selecionada não é a correta."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Alternativa não encontrada."
            )
    })
    @PostMapping("/{challengeId}/answer")
    public ResponseEntity<String> answer(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @PathVariable Long challengeId,
            @Valid @RequestBody AnswerChallengeRequest request
    ) {
        service.answer(
                user,
                workspaceId,
                moduleId,
                challengeId,
                request
        );

        return ResponseEntity.ok("Desafio respondido com sucesso.");
    }

}
