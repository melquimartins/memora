package io.github.melquimartins.memora.domain.module;

import io.github.melquimartins.memora.domain.module.dto.CreateModuleRequest;
import io.github.melquimartins.memora.domain.module.dto.ModuleResponse;
import io.github.melquimartins.memora.domain.module.dto.UpdateModuleRequest;
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
        name = "Módulo",
        description = "Endpoints de gerenciamento de módulos"
)
@RestController
@RequestMapping("/api/workspaces/{workspaceId}/modules")
public class ModuleController {

    private final ModuleService service;

    public ModuleController(ModuleService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cria um novo módulo",
            description = "Cria um módulo a partir dos dados informados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Módulo criado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de requisição inválidos."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Área de trabalho não encontrada."
            )
    })
    @PostMapping
    public ResponseEntity<ResponseEnvelope<ModuleResponse>> create(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @Valid @RequestBody CreateModuleRequest request
    ) {
        ModuleResponse collection = service.create(user, workspaceId, request);

        ResponseEnvelope<ModuleResponse> response = new ResponseEnvelope<>(
                "Módulo criado com sucesso.",
                collection
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Busca todos os módulos",
            description = "Busca todos os módulos disponíveis para a área de trabalho"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Módulos recuperados com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum módulo encontrado."
            )
    })
    @GetMapping
    public ResponseEntity<ResponseEnvelope<List<ModuleResponse>>> getAll(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId
    ) {
        List<ModuleResponse> collections = service.getAll(user, workspaceId);

        ResponseEnvelope<List<ModuleResponse>> response = new ResponseEnvelope<>(
                "Módulos recuperados com sucesso.",
                collections
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Busca um módulo",
            description = "Busca um módulo específico pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Módulo encontrado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum módulo encontrado."
            )
    })
    @GetMapping("/{moduleId}")
    public ResponseEntity<ResponseEnvelope<ModuleResponse>> get(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId
    ) {
        ModuleResponse collection = service.get(user, workspaceId, moduleId);

        ResponseEnvelope<ModuleResponse> response = new ResponseEnvelope<>(
                "Módulo encontrado com sucesso.",
                collection
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualiza informações de um módulo",
            description = "Atualiza informações de um módulo pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Módulo atualizado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Módulo não encontrado."
            )
    })
    @PatchMapping("/{moduleId}")
    public ResponseEntity<ResponseEnvelope<ModuleResponse>> update(
            @AuthenticationPrincipal User user,
            @PathVariable Long workspaceId,
            @PathVariable Long moduleId,
            @Valid @RequestBody UpdateModuleRequest request
    ) {
        ModuleResponse collection = service.update(
                user,
                workspaceId,
                moduleId,
                request
        );

        ResponseEnvelope<ModuleResponse> response = new ResponseEnvelope<>(
                "Módulo atualizado com sucesso.",
                collection
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Deleta um módulo",
            description = "Deleta um módulo pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Módulo deletado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Módulo não encontrado."
            )
    })
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
