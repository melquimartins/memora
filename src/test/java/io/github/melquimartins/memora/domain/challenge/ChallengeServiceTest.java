package io.github.melquimartins.memora.domain.challenge;

import io.github.melquimartins.memora.domain.alternative.Alternative;
import io.github.melquimartins.memora.domain.alternative.AlternativeRepository;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeRequest;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeResponse;
import io.github.melquimartins.memora.domain.challenge.mapper.ChallengeMapper;
import io.github.melquimartins.memora.domain.module.Module;
import io.github.melquimartins.memora.domain.module.ModuleRepository;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.shared.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private AlternativeRepository alternativeRepository;

    @Mock
    private ChallengeRepository repository;

    @Mock
    private ChallengeMapper mapper;

    @InjectMocks
    private ChallengeService service;

    @Test
    @DisplayName("Deve criar um desafio com sucesso")
    void shouldCreateChallengeSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;
        Module module = new Module("Módulo", "Descrição");

        ChallengeRequest request = new ChallengeRequest("Desafio");

        Challenge challenge = new Challenge(request.title());
        challenge.setModule(module);

        when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
        )).thenReturn(Optional.of(module));
        when(repository.save(any(Challenge.class))).thenReturn(challenge);

        ChallengeResponse expectedResponse = new ChallengeResponse(
                1L,
                UUID.randomUUID(),
                challenge.getTitle(),
                1,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(mapper.toResponse(any(Challenge.class))).thenReturn(expectedResponse);

        ChallengeResponse response = service.create(
                user,
                workspaceId,
                moduleId,
                request
        );

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Desafio", response.title());
        verify(moduleRepository)
                .findByIdAndWorkspaceIdAndWorkspaceUserId(
                        moduleId,
                        workspaceId,
                        user.getId()
                );
        verify(repository).save(any(Challenge.class));
        verify(mapper).toResponse(any(Challenge.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar desafio em módulo inexistente")
    void shouldThrowExceptionWhenModuleNotFoundOnCreate() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Long moduleId = 20L;

        ChallengeRequest request = new ChallengeRequest("Desafio");

        when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
        )).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.create(user, workspaceId, moduleId, request)
        );

        assertEquals("Módulo não encontrado.", exception.getMessage());
        verify(moduleRepository)
                .findByIdAndWorkspaceIdAndWorkspaceUserId(
                        moduleId,
                        workspaceId,
                        user.getId()
                );
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve buscar todos os desafios com sucesso")
    void shouldGetAllChallengesSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Challenge c1 = new Challenge("Desafio 1");
        List<Challenge> challenges = List.of(c1);

        when(repository.findAllByModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
        )).thenReturn(Optional.of(challenges));

        ChallengeResponse r1 = new ChallengeResponse(
                1L,
                UUID.randomUUID(),
                c1.getTitle(),
                1,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<ChallengeResponse> expectedResponses = List.of(r1);

        when(mapper.toResponseList(challenges)).thenReturn(expectedResponses);

        List<ChallengeResponse> response = service.getAll(
                user,
                workspaceId,
                moduleId
        );

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Desafio 1", response.getFirst().title());
        verify(repository).findAllByModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
        );
        verify(mapper).toResponseList(challenges);
    }

    @Test
    @DisplayName("Deve obter um desafio específico por ID com sucesso")
    void shouldGetChallengeByIdSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio");

        when(repository.findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        )).thenReturn(Optional.of(challenge));

        ChallengeResponse expectedResponse = new ChallengeResponse(
                challengeId,
                UUID.randomUUID(),
                challenge.getTitle(),
                1,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(mapper.toResponse(challenge)).thenReturn(expectedResponse);

        ChallengeResponse response = service.get(
                user,
                workspaceId,
                moduleId,
                challengeId
        );

        assertNotNull(response);
        assertEquals(challengeId, response.id());
        verify(repository).findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        );
        verify(mapper).toResponse(challenge);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar desafio inexistente")
    void shouldThrowExceptionWhenChallengeNotFoundOnGet() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;

        when(repository.findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        )).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.get(user, workspaceId, moduleId, challengeId)
        );

        assertEquals("Desafio não encontrado.", exception.getMessage());
        verify(repository).findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        );
    }

    @Test
    @DisplayName("Deve atualizar um desafio com sucesso")
    void shouldUpdateChallengeSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio Antigo");

        ChallengeRequest request = new ChallengeRequest("Desafio Novo");

        when(repository.findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        )).thenReturn(Optional.of(challenge));
        when(repository.save(challenge)).thenReturn(challenge);

        ChallengeResponse expectedResponse = new ChallengeResponse(
                challengeId,
                UUID.randomUUID(),
                request.title(),
                1,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(mapper.toResponse(challenge)).thenReturn(expectedResponse);

        ChallengeResponse response = service.update(
                user,
                workspaceId,
                moduleId,
                challengeId,
                request
        );

        assertNotNull(response);
        assertEquals("Desafio Novo", response.title());
        verify(repository).findByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        );
        verify(repository).save(challenge);
        verify(mapper).toResponse(challenge);
    }

    @Test
    @DisplayName("Deve deletar um desafio com sucesso")
    void shouldDeleteChallengeSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;

        when(repository.deleteByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        )).thenReturn(1L);

        assertDoesNotThrow(() -> service.delete(user, workspaceId, moduleId, challengeId));
        verify(repository).deleteByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar desafio inexistente")
    void shouldThrowExceptionWhenChallengeNotFoundOnDelete() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;

        Long moduleId = 20L;

        Long challengeId = 30L;

        when(repository.deleteByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        )).thenReturn(0L);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.delete(user, workspaceId, moduleId, challengeId)
        );

        assertEquals("Desafio não encontrado.", exception.getMessage());
        verify(repository).deleteByIdAndModuleIdAndModuleWorkspaceIdAndModuleWorkspaceUserId(
                challengeId,
                moduleId,
                workspaceId,
                user.getId()
        );
    }

    @Test
    @DisplayName("Deve responder um desafio com sucesso quando a alternativa for correta")
    void shouldAnswerChallengeSuccessfully() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Long moduleId = 20L;
        Module module = new Module("Módulo", "Descrição");

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio");
        challenge.setModule(module);

        Long alternativeId = 40L;
        Alternative alternative = new Alternative("Alternativa Correta", true);

        io.github.melquimartins.memora.domain.challenge.dto.AnswerChallengeRequest request =
                new io.github.melquimartins.memora.domain.challenge.dto.AnswerChallengeRequest(
                        alternativeId,
                        io.github.melquimartins.memora.domain.challenge.enums.DifficultyLevel.EASY
                );

        when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId()))
                .thenReturn(Optional.of(module));
        when(repository.findByIdAndModuleId(challengeId, moduleId))
                .thenReturn(Optional.of(challenge));
        when(alternativeRepository.findByIdAndChallengeId(alternativeId, challengeId))
                .thenReturn(Optional.of(alternative));
        when(repository.save(challenge)).thenReturn(challenge);

        assertDoesNotThrow(() -> service.answer(user, workspaceId, moduleId, challengeId, request));

        verify(repository).save(challenge);
    }

    @Test
    @DisplayName("Deve lançar BadRequestException ao responder com alternativa incorreta")
    void shouldThrowBadRequestExceptionWhenAlternativeIsIncorrectOnAnswer() {
        User user = new User("Nome do Usuário", "usuario@email.com", "senha123");
        user.setId(1L);

        Long workspaceId = 10L;
        Long moduleId = 20L;
        Module module = new Module("Módulo", "Descrição");

        Long challengeId = 30L;
        Challenge challenge = new Challenge("Desafio");
        challenge.setModule(module);

        Long alternativeId = 40L;
        Alternative alternative = new Alternative("Alternativa Incorreta", false);

        io.github.melquimartins.memora.domain.challenge.dto.AnswerChallengeRequest request =
                new io.github.melquimartins.memora.domain.challenge.dto.AnswerChallengeRequest(
                        alternativeId,
                        io.github.melquimartins.memora.domain.challenge.enums.DifficultyLevel.EASY
                );

        when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(moduleId, workspaceId, user.getId()))
                .thenReturn(Optional.of(module));
        when(repository.findByIdAndModuleId(challengeId, moduleId))
                .thenReturn(Optional.of(challenge));
        when(alternativeRepository.findByIdAndChallengeId(alternativeId, challengeId))
                .thenReturn(Optional.of(alternative));

        io.github.melquimartins.memora.shared.exception.BadRequestException exception = assertThrows(
                io.github.melquimartins.memora.shared.exception.BadRequestException.class,
                () -> service.answer(user, workspaceId, moduleId, challengeId, request)
        );

        assertEquals("A alternativa selecionada não é a correta.", exception.getMessage());
        verify(repository, never()).save(any());
    }

}
