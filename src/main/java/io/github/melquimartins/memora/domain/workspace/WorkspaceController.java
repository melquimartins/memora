package io.github.melquimartins.memora.domain.workspace;

import io.github.melquimartins.memora.shared.dto.ResponseEnvelope;
import io.github.melquimartins.memora.domain.workspace.dto.CreateWorkspaceRequest;
import io.github.melquimartins.memora.domain.workspace.dto.UpdateWorkspaceRequest;
import io.github.melquimartins.memora.domain.workspace.dto.WorkspaceResponse;
import io.github.melquimartins.memora.domain.user.User;
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
        name = "Área de Trabalho",
        description = "Endpoints de gerenciamento de áreas de trabalho"
)
@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService service;

    public WorkspaceController(WorkspaceService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cria uma nova área de trabalho",
            description = "Cria uma área de trabalho a partir dos dados informados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Área de trabalho criada com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de requisição inválidos."
            ),
    })
    @PostMapping
    public ResponseEntity<ResponseEnvelope<WorkspaceResponse>> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateWorkspaceRequest request
    ) {
        WorkspaceResponse workspace = service.create(user, request);

        ResponseEnvelope<WorkspaceResponse> response = new ResponseEnvelope<>(
                "Área de trabalho criada com sucesso.",
                workspace
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Busca todas as áreas de trabalho",
            description = "Busca todas as áreas de trabalho disponíveis para o usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Áreas de trabalho recuperadas com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhuma área de trabalho encontrada."
            )
    })
    @GetMapping
    public ResponseEntity<ResponseEnvelope<List<WorkspaceResponse>>> getAll(
            @AuthenticationPrincipal User user
    ) {
        List<WorkspaceResponse> workspaces = service.getAll(user);

        ResponseEnvelope<List<WorkspaceResponse>> response = new ResponseEnvelope<>(
                "Áreas de trabalho recuperadas com sucesso.",
                workspaces
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Busca uma área de trabalho",
            description = "Busca uma área de trabalho específica pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Área de trabalho recuperada com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhuma área de trabalho encontrada."
            ),
    })
    @GetMapping("/{workspaceId}")
    public ResponseEntity<ResponseEnvelope<WorkspaceResponse>> get(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId
    ) {
        WorkspaceResponse workspace = service.get(user, workspaceId);

        ResponseEnvelope<WorkspaceResponse> response = new ResponseEnvelope<>(
                "Área de trabalho recuperada com sucesso.",
                workspace
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualiza informações de uma área de trabalho",
            description = "Atualiza informações de uma área de trabalho pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Área de trabalho atualizada com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Área de trabalho não encontrada."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nenhum campo para atualizar foi informado."
            )
    })
    @PatchMapping("/{workspaceId}")
    public ResponseEntity<ResponseEnvelope<WorkspaceResponse>> update(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @Valid @RequestBody UpdateWorkspaceRequest request
    ) {
        WorkspaceResponse workspace = service.update(user, workspaceId, request);

        ResponseEnvelope<WorkspaceResponse> response = new ResponseEnvelope<>(
                "Área de trabalho atualizada com sucesso.",
                workspace
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Deleta uma área de trabalho",
            description = "Deleta uma área de trabalho pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Área de trabalho deletada com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Área de trabalho não encontrada."
            )
    })
    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<String> delete(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId
    ) {
        service.delete(user, workspaceId);

        return ResponseEntity.ok().body("Área de trabalho deletada com sucesso.");
    }

}
