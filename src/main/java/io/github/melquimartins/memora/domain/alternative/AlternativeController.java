package io.github.melquimartins.memora.domain.alternative;

import io.github.melquimartins.memora.domain.alternative.dto.AlternativeResponse;
import io.github.melquimartins.memora.domain.alternative.dto.CreateAlternativeRequest;
import io.github.melquimartins.memora.domain.alternative.dto.UpdateAlternativeRequest;
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
        name = "Alternativa",
        description = "Endpoints de gerenciamento de alternativas"
)
@RestController
@RequestMapping(
        "/api/workspaces/{workspaceId}/modules/{moduleId}/challenges/{challengeId}/alternatives"
)
public class AlternativeController {

    private final AlternativeService service;

    public AlternativeController(AlternativeService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cria uma nova alternativa",
            description = "Cria uma alternativa a partir dos dados informados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Alternativa criada com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de requisição inválidos."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Desafio não encontrado."
            )
    })
    @PostMapping
    public ResponseEntity<ResponseEnvelope<AlternativeResponse>> create(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @PathVariable Long challengeId,
            @Valid @RequestBody CreateAlternativeRequest request
    ) {
        AlternativeResponse alternative = service.create(
                user,
                workspaceId,
                moduleId,
                challengeId,
                request
        );

        ResponseEnvelope<AlternativeResponse> response = new ResponseEnvelope<>(
                "Alternativa criada com sucesso.",
                alternative
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Busca todas as alternativas",
            description = "Busca todas as alternativas disponíveis para o desafio"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alternativas recuperadas com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhuma alternativa encontrada."
            )
    })
    @GetMapping
    public ResponseEntity<ResponseEnvelope<List<AlternativeResponse>>> getAll(
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

        ResponseEnvelope<List<AlternativeResponse>> response = new ResponseEnvelope<>(
                "Alternativas recuperadas com sucesso.",
                alternatives
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Busca uma alternativa",
            description = "Busca uma alternativa específica pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alternativa encontrada com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhuma alternativa encontrada."
            )
    })
    @GetMapping("/{alternativeId}")
    public ResponseEntity<ResponseEnvelope<AlternativeResponse>> get(
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

        ResponseEnvelope<AlternativeResponse> response = new ResponseEnvelope<>(
                "Alternativa encontrada com sucesso.",
                alternative
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualiza informações de uma alternativa",
            description = "Atualiza informações de uma alternativa pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alternativa atualizada com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Alternativa não encontrada."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nenhum campo para atualizar foi informado."
            )
    })
    @PatchMapping("/{alternativeId}")
    public ResponseEntity<ResponseEnvelope<AlternativeResponse>> update(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @PathVariable Long challengeId,
            @PathVariable Long alternativeId,
            @Valid @RequestBody UpdateAlternativeRequest request
    ) {
        AlternativeResponse alternative = service.update(
                user,
                workspaceId,
                moduleId,
                challengeId,
                alternativeId,
                request
        );

        ResponseEnvelope<AlternativeResponse> response = new ResponseEnvelope<>(
                "Alternativa atualizada com sucesso.",
                alternative
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Deleta uma alternativa",
            description = "Deleta uma alternativa pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alternativa deletada com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Alternativa não encontrada."
            )
    })
    @DeleteMapping("/{alternativeId}")
    public ResponseEntity<String> delete(
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

        return ResponseEntity.ok().body("Alternativa deletada com sucesso.");
    }

}
