package io.github.melquimartins.memora.domain.module;

import io.github.melquimartins.memora.domain.module.dto.CreateModuleRequest;
import io.github.melquimartins.memora.domain.module.dto.ModuleResponse;
import io.github.melquimartins.memora.domain.module.dto.UpdateModuleRequest;
import io.github.melquimartins.memora.domain.module.mapper.ModuleMapper;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.domain.workspace.Workspace;
import io.github.melquimartins.memora.domain.workspace.WorkspaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private ModuleRepository repository;

    @Mock
    private ModuleMapper mapper;

    @InjectMocks
    private ModuleService service;

    @Test
    @DisplayName("Deve criar um módulo com sucesso")
    void shouldCreateModuleSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Workspace workspace = new Workspace("Workspace", "Descrição");

        CreateModuleRequest request = new CreateModuleRequest("Módulo", "Descrição");

        Module module = new Module(request.title(), request.description());
        module.setWorkspace(workspace);

        when(workspaceRepository.findByIdAndUserId(workspaceId, user.getId())).thenReturn(Optional.of(workspace));
        when(repository.save(any(Module.class))).thenReturn(module);

        ModuleResponse expectedResponse = new ModuleResponse(
                1L, UUID.randomUUID(), module.getTitle(),
                module.getDescription(), LocalDateTime.now(), LocalDateTime.now()
        );

        when(mapper.toResponse(any(Module.class))).thenReturn(expectedResponse);

        ModuleResponse response = service.create(user, workspaceId, request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Módulo", response.title());
        verify(workspaceRepository).findByIdAndUserId(workspaceId, user.getId());
        verify(repository).save(any(Module.class));
        verify(mapper).toResponse(any(Module.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar módulo em workspace inexistente")
    void shouldThrowExceptionWhenWorkspaceNotFoundOnCreate() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        CreateModuleRequest request = new CreateModuleRequest("Módulo", "Descrição");

        when(workspaceRepository.findByIdAndUserId(workspaceId, user.getId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.create(user, workspaceId, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(workspaceRepository).findByIdAndUserId(workspaceId, user.getId());
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve buscar todos os módulos com sucesso")
    void shouldGetAllModulesSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Workspace workspace = new Workspace("Workspace", "Descrição");

        Module m1 = new Module("Módulo 1", "Descrição 1");
        List<Module> modules = List.of(m1);

        when(workspaceRepository.findByIdAndUserId(workspaceId, user.getId())).thenReturn(Optional.of(workspace));
        when(repository.findAllByWorkspaceId(workspaceId)).thenReturn(modules);

        ModuleResponse r1 = new ModuleResponse(
                1L, UUID.randomUUID(), m1.getTitle(), m1.getDescription(),
                LocalDateTime.now(), LocalDateTime.now()
        );
        List<ModuleResponse> expectedResponses = List.of(r1);

        when(mapper.toResponseList(modules)).thenReturn(expectedResponses);

        List<ModuleResponse> response = service.getAll(user, workspaceId);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Módulo 1", response.getFirst().title());
        verify(workspaceRepository).findByIdAndUserId(workspaceId, user.getId());
        verify(repository).findAllByWorkspaceId(workspaceId);
        verify(mapper).toResponseList(modules);
    }

    @Test
    @DisplayName("Deve obter um módulo específico por ID com sucesso")
    void shouldGetModuleByIdSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Workspace workspace = new Workspace("Workspace", "Descrição");

        Long moduleId = 20L;
        Module module = new Module("Módulo", "Descrição");

        when(workspaceRepository.findByIdAndUserId(workspaceId, user.getId())).thenReturn(Optional.of(workspace));
        when(repository.findByIdAndWorkspaceId(moduleId, workspaceId)).thenReturn(Optional.of(module));

        ModuleResponse expectedResponse = new ModuleResponse(
                moduleId, UUID.randomUUID(), module.getTitle(), module.getDescription(), LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(mapper.toResponse(module)).thenReturn(expectedResponse);

        ModuleResponse response = service.get(user, workspaceId, moduleId);

        assertNotNull(response);
        assertEquals(moduleId, response.id());
        verify(workspaceRepository).findByIdAndUserId(workspaceId, user.getId());
        verify(repository).findByIdAndWorkspaceId(moduleId, workspaceId);
        verify(mapper).toResponse(module);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar módulo inexistente")
    void shouldThrowExceptionWhenModuleNotFoundOnGet() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Workspace workspace = new Workspace("Workspace", "Descrição");

        Long moduleId = 20L;

        when(workspaceRepository.findByIdAndUserId(workspaceId, user.getId())).thenReturn(Optional.of(workspace));
        when(repository.findByIdAndWorkspaceId(moduleId, workspaceId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.get(user, workspaceId, moduleId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Módulo não encontrado.", exception.getReason());
        verify(workspaceRepository).findByIdAndUserId(workspaceId, user.getId());
        verify(repository).findByIdAndWorkspaceId(moduleId, workspaceId);
    }

    @Test
    @DisplayName("Deve atualizar um módulo com sucesso")
    void shouldUpdateModuleSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Workspace workspace = new Workspace("Workspace", "Descrição");

        Long moduleId = 20L;
        Module module = new Module("Módulo Antigo", "Descrição Antiga");

        UpdateModuleRequest request = new UpdateModuleRequest("Módulo Novo", "Descrição Nova");

        when(workspaceRepository.findByIdAndUserId(workspaceId, user.getId())).thenReturn(Optional.of(workspace));
        when(repository.findByIdAndWorkspaceId(moduleId, workspaceId)).thenReturn(Optional.of(module));
        when(repository.save(module)).thenReturn(module);

        ModuleResponse expectedResponse = new ModuleResponse(
                moduleId, UUID.randomUUID(), request.title(),
                request.description(), LocalDateTime.now(), LocalDateTime.now()
        );

        when(mapper.toResponse(module)).thenReturn(expectedResponse);

        ModuleResponse response = service.update(user, workspaceId, moduleId, request);

        assertNotNull(response);
        assertEquals("Módulo Novo", response.title());
        verify(workspaceRepository).findByIdAndUserId(workspaceId, user.getId());
        verify(repository).findByIdAndWorkspaceId(moduleId, workspaceId);
        verify(repository).save(module);
        verify(mapper).toResponse(module);
    }

    @Test
    @DisplayName("Deve deletar um módulo com sucesso")
    void shouldDeleteModuleSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Workspace workspace = new Workspace("Workspace", "Descrição");

        Long moduleId = 20L;

        when(workspaceRepository.findByIdAndUserId(workspaceId, user.getId())).thenReturn(Optional.of(workspace));
        when(repository.deleteByIdAndWorkspaceId(moduleId, workspaceId)).thenReturn(1L);

        assertDoesNotThrow(() -> service.delete(user, workspaceId, moduleId));

        verify(workspaceRepository).findByIdAndUserId(workspaceId, user.getId());
        verify(repository).deleteByIdAndWorkspaceId(moduleId, workspaceId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar módulo inexistente")
    void shouldThrowExceptionWhenModuleNotFoundOnDelete() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Workspace workspace = new Workspace("Workspace", "Descrição");

        Long moduleId = 20L;

        when(workspaceRepository.findByIdAndUserId(workspaceId, user.getId())).thenReturn(Optional.of(workspace));
        when(repository.deleteByIdAndWorkspaceId(moduleId, workspaceId)).thenReturn(0L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.delete(user, workspaceId, moduleId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(workspaceRepository).findByIdAndUserId(workspaceId, user.getId());
        verify(repository).deleteByIdAndWorkspaceId(moduleId, workspaceId);
    }

}
