package io.github.melquimartins.memora.domain.workspace;

import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.domain.workspace.dto.CreateWorkspaceRequest;
import io.github.melquimartins.memora.domain.workspace.dto.UpdateWorkspaceRequest;
import io.github.melquimartins.memora.domain.workspace.dto.WorkspaceResponse;
import io.github.melquimartins.memora.domain.workspace.mapper.WorkspaceMapper;
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
class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository repository;

    @Mock
    private WorkspaceMapper mapper;

    @InjectMocks
    private WorkspaceService service;

    @Test
    @DisplayName("Deve criar um workspace com sucesso")
    void shouldCreateWorkspaceSuccessfully() {
        User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
        user.setId(1L);

        CreateWorkspaceRequest request = new CreateWorkspaceRequest(
                "Workspace",
                "Descrição"
        );

        Workspace workspace = new Workspace(
                request.title(),
                request.description()
        );
        workspace.setUser(user);

        when(repository.save(any(Workspace.class))).thenReturn(workspace);

        WorkspaceResponse expectedResponse = new WorkspaceResponse(
                1L,
                UUID.randomUUID(),
                workspace.getTitle(),
                workspace.getDescription(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(mapper.toResponse(any(Workspace.class))).thenReturn(expectedResponse);

        WorkspaceResponse response = service.create(user, request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertNotNull(response.uuid());
        assertEquals("Workspace", response.title());
        verify(repository, times(1)).save(any(Workspace.class));
        verify(mapper, times(1)).toResponse(any(Workspace.class));
    }

    @Test
    @DisplayName("Deve buscar todos os workspaces de um usuário")
    void shouldGetAllWorkspacesSuccessfully() {
        User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
        user.setId(1L);

        Workspace w1 = new Workspace("Workspace 1", "Descrição 1");
        Workspace w2 = new Workspace("Workspace 2", "Descrição 2");
        List<Workspace> workspaces = List.of(w1, w2);

        WorkspaceResponse r1 = new WorkspaceResponse(
                1L,
                UUID.randomUUID(),
                w1.getTitle(),
                w1.getDescription(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        WorkspaceResponse r2 = new WorkspaceResponse(
                2L,
                UUID.randomUUID(),
                w2.getTitle(),
                w2.getDescription(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        List<WorkspaceResponse> expectedResponses = List.of(r1, r2);

        when(repository.findAllByUserId(user.getId())).thenReturn(workspaces);
        when(mapper.toResponseList(workspaces)).thenReturn(expectedResponses);

        List<WorkspaceResponse> response = service.getAll(user);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Workspace 1", response.getFirst().title());
        verify(repository, times(1)).findAllByUserId(user.getId());
        verify(mapper, times(1)).toResponseList(workspaces);
    }

    @Test
    @DisplayName("Deve obter um workspace específico por ID com sucesso")
    void shouldGetWorkspaceByIdSuccessfully() {
        User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
        user.setId(1L);

        Long workspaceId = 10L;
        Workspace workspace = new Workspace("Workspace 10", "Descrição 10");
        workspace.setUser(user);

        WorkspaceResponse expectedResponse = new WorkspaceResponse(
                workspaceId,
                UUID.randomUUID(),
                workspace.getTitle(),
                workspace.getDescription(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(repository.findByIdAndUserId(workspaceId, user.getId()))
                .thenReturn(Optional.of(workspace));
        when(mapper.toResponse(workspace)).thenReturn(expectedResponse);

        WorkspaceResponse response = service.get(user, workspaceId);

        assertNotNull(response);
        assertEquals(workspaceId, response.id());
        verify(repository, times(1)).findByIdAndUserId(workspaceId, user.getId());
        verify(mapper, times(1)).toResponse(workspace);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar workspace por ID inexistente")
    void shouldThrowExceptionWhenWorkspaceNotFoundOnGet() {
        User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
        user.setId(1L);

        Long workspaceId = 10L;

        when(repository.findByIdAndUserId(workspaceId, user.getId()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.get(user, workspaceId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Área de trabalho não encontrada.", exception.getReason());
        verify(repository, times(1)).findByIdAndUserId(workspaceId, user.getId());
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Deve atualizar um workspace com sucesso")
    void shouldUpdateWorkspaceSuccessfully() {
        User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
        user.setId(1L);

        Long workspaceId = 10L;
        Workspace workspace = new Workspace("Antigo Titulo", "Antiga Descrição");
        workspace.setUser(user);

        UpdateWorkspaceRequest request = new UpdateWorkspaceRequest(
                "Novo Titulo",
                "Nova Descrição"
        );

        WorkspaceResponse expectedResponse = new WorkspaceResponse(
                workspaceId,
                UUID.randomUUID(),
                request.title(),
                request.description(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(repository.findByIdAndUserId(workspaceId, user.getId()))
                .thenReturn(Optional.of(workspace));
        when(repository.save(workspace)).thenReturn(workspace);
        when(mapper.toResponse(workspace)).thenReturn(expectedResponse);

        WorkspaceResponse response = service.update(user, workspaceId, request);

        assertNotNull(response);
        assertEquals("Novo Titulo", response.title());
        assertEquals("Nova Descrição", response.description());
        verify(repository, times(1)).findByIdAndUserId(workspaceId, user.getId());
        verify(repository, times(1)).save(workspace);
        verify(mapper, times(1)).toResponse(workspace);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar workspace inexistente")
    void shouldThrowExceptionWhenWorkspaceNotFoundOnUpdate() {
        User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
        user.setId(1L);

        Long workspaceId = 10L;

        UpdateWorkspaceRequest request = new UpdateWorkspaceRequest(
                "Novo Titulo",
                "Nova Descrição"
        );

        when(repository.findByIdAndUserId(workspaceId, user.getId()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.update(user, workspaceId, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(repository, times(1)).findByIdAndUserId(workspaceId, user.getId());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar um workspace com sucesso")
    void shouldDeleteWorkspaceSuccessfully() {
        User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
        user.setId(1L);

        Long workspaceId = 10L;

        when(repository.deleteByIdAndUserId(workspaceId, user.getId()))
                .thenReturn(1L);
        assertDoesNotThrow(() -> service.delete(user, workspaceId));
        verify(repository, times(1))
                .deleteByIdAndUserId(workspaceId, user.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar workspace inexistente")
    void shouldThrowExceptionWhenWorkspaceNotFoundOnDelete() {
        User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
        user.setId(1L);

        Long workspaceId = 10L;

        when(repository.deleteByIdAndUserId(workspaceId, user.getId()))
                .thenReturn(0L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> service.delete(user, workspaceId)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(repository, times(1)).deleteByIdAndUserId(workspaceId, user.getId());
    }

}