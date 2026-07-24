package io.github.melquimartins.memora.domain.alternative;

import io.github.melquimartins.memora.domain.alternative.dto.AlternativeResponse;
import io.github.melquimartins.memora.domain.alternative.dto.CreateAlternativeRequest;
import io.github.melquimartins.memora.domain.alternative.dto.UpdateAlternativeRequest;
import io.github.melquimartins.memora.domain.alternative.mapper.AlternativeMapper;
import io.github.melquimartins.memora.domain.challenge.Challenge;
import io.github.melquimartins.memora.domain.challenge.ChallengeRepository;
import io.github.melquimartins.memora.domain.user.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlternativeServiceTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private AlternativeRepository repository;

    @Mock
    private AlternativeMapper mapper;

    @InjectMocks
    private AlternativeService service;

    @Test
    @DisplayName("Deve criar uma alternativa com sucesso")
    void shouldCreateAlternativeSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio");

        CreateAlternativeRequest request = new CreateAlternativeRequest(
                "Alternativa",
                true
        );

        Alternative alternative = new Alternative(
                request.text(),
                request.correct()
        );
        alternative.setChallenge(challenge);

        when(challengeRepository
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                )).thenReturn(Optional.of(challenge));
        when(repository.save(any(Alternative.class))).thenReturn(alternative);

        AlternativeResponse expectedResponse = new AlternativeResponse(
                1L,
                alternative.getText(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(mapper.toResponse(any(Alternative.class))).thenReturn(expectedResponse);

        AlternativeResponse response = service.create(
                user,
                workspaceId,
                moduleId,
                challengeId,
                request
        );

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertTrue(response.correct());
        verify(challengeRepository)
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                );
        verify(repository).save(any(Alternative.class));
        verify(mapper).toResponse(any(Alternative.class));
    }

    @Test
    @DisplayName("Deve buscar todas as alternativas com sucesso")
    void shouldGetAllAlternativesSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio");

        Alternative a1 = new Alternative("Alternativa 1", true);
        List<Alternative> alternatives = List.of(a1);

        when(challengeRepository
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                )).thenReturn(Optional.of(challenge));
        when(repository.findAllByChallengeId(challengeId)).thenReturn(alternatives);

        AlternativeResponse r1 = new AlternativeResponse(
                1L,
                a1.getText(),
                a1.getCorrect(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        List<AlternativeResponse> expectedResponses = List.of(r1);

        when(mapper.toResponseList(alternatives)).thenReturn(expectedResponses);

        List<AlternativeResponse> response = service.getAll(
                user,
                workspaceId,
                moduleId,
                challengeId
        );

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Alternativa 1", response.getFirst().text());
        verify(challengeRepository)
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                );
        verify(repository).findAllByChallengeId(challengeId);
        verify(mapper).toResponseList(alternatives);
    }

    @Test
    @DisplayName("Deve obter uma alternativa por ID com sucesso")
    void shouldGetAlternativeByIdSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio");

        Long alternativeId = 40L;
        Alternative alternative = new Alternative("Alternativa", true);

        when(challengeRepository
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                )).thenReturn(Optional.of(challenge));
        when(repository.findByIdAndChallengeId(alternativeId, challengeId))
                .thenReturn(Optional.of(alternative));

        AlternativeResponse expectedResponse = new AlternativeResponse(
                alternativeId,
                alternative.getText(),
                alternative.getCorrect(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(mapper.toResponse(alternative)).thenReturn(expectedResponse);

        AlternativeResponse response = service.get(
                user,
                workspaceId,
                moduleId,
                challengeId,
                alternativeId
        );

        assertNotNull(response);
        assertEquals(alternativeId, response.id());
        verify(challengeRepository)
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                );
        verify(repository)
                .findByIdAndChallengeId(alternativeId, challengeId);
        verify(mapper).toResponse(alternative);
    }

    @Test
    @DisplayName("Deve atualizar uma alternativa com sucesso")
    void shouldUpdateAlternativeSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio");

        Long alternativeId = 40L;
        Alternative alternative = new Alternative("Alternativa Antiga", false);

        UpdateAlternativeRequest request = new UpdateAlternativeRequest(
                "Alternativa Nova",
                true
        );

        when(challengeRepository
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                )).thenReturn(Optional.of(challenge));
        when(repository.findByIdAndChallengeId(alternativeId, challengeId))
                .thenReturn(Optional.of(alternative));
        when(repository.save(alternative)).thenReturn(alternative);

        AlternativeResponse expectedResponse = new AlternativeResponse(
                alternativeId,
                request.text(),
                request.correct(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(mapper.toResponse(alternative)).thenReturn(expectedResponse);

        AlternativeResponse response = service.update(
                user,
                workspaceId,
                moduleId,
                challengeId,
                alternativeId,
                request
        );

        assertNotNull(response);
        assertEquals("Alternativa Nova", response.text());
        assertTrue(response.correct());
        verify(challengeRepository)
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                );
        verify(repository)
                .findByIdAndChallengeId(alternativeId, challengeId);
        verify(repository).save(alternative);
        verify(mapper).toResponse(alternative);
    }

    @Test
    @DisplayName("Deve deletar uma alternativa com sucesso")
    void shouldDeleteAlternativeSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio");

        Long alternativeId = 40L;

        when(challengeRepository
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                )).thenReturn(Optional.of(challenge));
        when(repository.deleteByIdAndChallengeId(alternativeId, challengeId))
                .thenReturn(1L);

        assertDoesNotThrow(() -> service.delete(
                user,
                workspaceId,
                moduleId,
                challengeId,
                alternativeId
        ));

        verify(challengeRepository)
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                );
        verify(repository)
                .deleteByIdAndChallengeId(alternativeId, challengeId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar alternativa inexistente")
    void shouldThrowExceptionWhenAlternativeNotFoundOnDelete() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio");

        Long alternativeId = 40L;

        when(challengeRepository
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                )).thenReturn(Optional.of(challenge));
        when(repository.deleteByIdAndChallengeId(alternativeId, challengeId))
                .thenReturn(0L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.delete(
                        user,
                        workspaceId,
                        moduleId,
                        challengeId,
                        alternativeId
                )
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(challengeRepository)
                .findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                        challengeId,
                        moduleId,
                        workspaceId,
                        user.getId()
                );
        verify(repository)
                .deleteByIdAndChallengeId(alternativeId, challengeId);
    }

}
