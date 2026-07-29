package io.github.melquimartins.memora.domain.module;

import io.github.melquimartins.memora.domain.module.dto.CreateModuleRequest;
import io.github.melquimartins.memora.domain.module.dto.ModuleResponse;
import io.github.melquimartins.memora.domain.module.dto.UpdateModuleRequest;
import io.github.melquimartins.memora.domain.module.mapper.ModuleMapper;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.domain.workspace.Workspace;
import io.github.melquimartins.memora.domain.workspace.WorkspaceRepository;
import io.github.melquimartins.memora.shared.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.create(user, workspaceId, request)
        );

        assertEquals("Área de trabalho não encontrada.", exception.getMessage());
        verify(workspaceRepository).findByIdAndUserId(workspaceId, user.getId());
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve buscar todos os módulos com sucesso")
    void shouldGetAllModulesSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Module m1 = new Module("Módulo 1", "Descrição 1");
        List<Module> modules = List.of(m1);

        when(repository.findAllByWorkspaceIdAndWorkspaceUserId(workspaceId, user.getId())).thenReturn(Optional.of(modules));

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
        verify(repository).findAllByWorkspaceIdAndWorkspaceUserId(workspaceId, user.getId());
        verify(mapper).toResponseList(modules);
    }

    @Test
    @DisplayName("Deve obter um módulo específico por ID com sucesso")
    void shouldGetModuleByIdSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Long moduleId = 20L;
        Module module = new Module("Módulo", "Descrição");

        when(repository.findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId())).thenReturn(Optional.of(module));

        ModuleResponse expectedResponse = new ModuleResponse(
                moduleId, UUID.randomUUID(), module.getTitle(), module.getDescription(), LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(mapper.toResponse(module)).thenReturn(expectedResponse);

        ModuleResponse response = service.get(user, workspaceId, moduleId);

        assertNotNull(response);
        assertEquals(moduleId, response.id());
        verify(repository).findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId());
        verify(mapper).toResponse(module);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar módulo inexistente")
    void shouldThrowExceptionWhenModuleNotFoundOnGet() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Long moduleId = 20L;

        when(repository.findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.get(user, workspaceId, moduleId)
        );

        assertEquals("Módulo não encontrado.", exception.getMessage());
        verify(repository).findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId());
    }

    @Test
    @DisplayName("Deve atualizar um módulo com sucesso")
    void shouldUpdateModuleSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Long moduleId = 20L;
        Module module = new Module("Módulo Antigo", "Descrição Antiga");

        UpdateModuleRequest request = new UpdateModuleRequest("Módulo Novo", "Descrição Nova");

        when(repository.findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId())).thenReturn(Optional.of(module));
        when(repository.save(module)).thenReturn(module);

        ModuleResponse expectedResponse = new ModuleResponse(
                moduleId, UUID.randomUUID(), request.title(),
                request.description(), LocalDateTime.now(), LocalDateTime.now()
        );

        when(mapper.toResponse(module)).thenReturn(expectedResponse);

        ModuleResponse response = service.update(user, workspaceId, moduleId, request);

        assertNotNull(response);
        assertEquals("Módulo Novo", response.title());
        verify(repository).findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId());
        verify(repository).save(module);
        verify(mapper).toResponse(module);
    }

    @Test
    @DisplayName("Deve deletar um módulo com sucesso")
    void shouldDeleteModuleSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Long moduleId = 20L;

        when(repository.deleteByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId())).thenReturn(1L);

        assertDoesNotThrow(() -> service.delete(user, workspaceId, moduleId));

        verify(repository).deleteByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar módulo inexistente")
    void shouldThrowExceptionWhenModuleNotFoundOnDelete() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Long moduleId = 20L;

        when(repository.deleteByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId())).thenReturn(0L);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.delete(user, workspaceId, moduleId)
        );

        assertEquals("Módulo não encontrado.", exception.getMessage());
        verify(repository).deleteByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId());
    }

}
