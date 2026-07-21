package io.github.melquimartins.memora.domain.challenge;

import io.github.melquimartins.memora.domain.challenge.dto.ChallengeRequest;
import io.github.melquimartins.memora.domain.challenge.dto.ChallengeResponse;
import io.github.melquimartins.memora.domain.challenge.mapper.ChallengeMapper;
import io.github.melquimartins.memora.domain.module.Module;
import io.github.melquimartins.memora.domain.module.ModuleRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

  @Mock
  private ModuleRepository moduleRepository;

  @Mock
  private ChallengeRepository repository;

  @Mock
  private ChallengeMapper mapper;

  @InjectMocks
  private ChallengeService service;

  @Test
  @DisplayName("Deve criar um desafio com sucesso")
  void shouldCreateChallengeSuccessfully() {
    User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
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
    verify(moduleRepository, times(1))
          .findByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
          );
    verify(repository, times(1)).save(any(Challenge.class));
    verify(mapper, times(1)).toResponse(any(Challenge.class));
  }

  @Test
  @DisplayName("Deve lançar exceção ao criar desafio em módulo inexistente")
  void shouldThrowExceptionWhenModuleNotFoundOnCreate() {
    User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
    user.setId(1L);

    Long workspaceId = 10L;
    Long moduleId = 20L;

    ChallengeRequest request = new ChallengeRequest("Desafio");

    when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
          moduleId,
          workspaceId,
          user.getId()
    )).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.create(user, workspaceId, moduleId, request)
    );

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    verify(moduleRepository, times(1))
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
    User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
    user.setId(1L);

    Long workspaceId = 10L;

    Long moduleId = 20L;
    Module module = new Module("Módulo", "Descrição");

    Challenge c1 = new Challenge("Desafio 1");
    List<Challenge> challenges = List.of(c1);

    when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
          moduleId,
          workspaceId,
          user.getId()
    )).thenReturn(Optional.of(module));
    when(repository.findAllByModuleId(moduleId)).thenReturn(challenges);

    ChallengeResponse r1 = new ChallengeResponse(
          1L,
          UUID.randomUUID(),
          c1.getTitle(),
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
    verify(moduleRepository, times(1))
          .findByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
          );
    verify(repository, times(1)).findAllByModuleId(moduleId);
    verify(mapper, times(1)).toResponseList(challenges);
  }

  @Test
  @DisplayName("Deve obter um desafio específico por ID com sucesso")
  void shouldGetChallengeByIdSuccessfully() {
    User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
    user.setId(1L);

    Long workspaceId = 10L;

    Long moduleId = 20L;
    Module module = new Module("Módulo", "Descrição");

    Long challengeId = 30L;
    Challenge challenge = new Challenge("Desafio");

    when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
          moduleId,
          workspaceId,
          user.getId()
    )).thenReturn(Optional.of(module));
    when(repository.findByIdAndModuleId(challengeId, moduleId))
          .thenReturn(Optional.of(challenge));

    ChallengeResponse expectedResponse = new ChallengeResponse(
          challengeId,
          UUID.randomUUID(),
          challenge.getTitle(),
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
    verify(moduleRepository, times(1))
          .findByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
          );
    verify(repository, times(1)).findByIdAndModuleId(challengeId, moduleId);
    verify(mapper, times(1)).toResponse(challenge);
  }

  @Test
  @DisplayName("Deve lançar exceção ao buscar desafio inexistente")
  void shouldThrowExceptionWhenChallengeNotFoundOnGet() {
    User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
    user.setId(1L);

    Long workspaceId = 10L;

    Long moduleId = 20L;
    Module module = new Module("Módulo", "Descrição");

    Long challengeId = 30L;

    when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
          moduleId,
          workspaceId,
          user.getId()
    )).thenReturn(Optional.of(module));
    when(repository.findByIdAndModuleId(
          challengeId,
          moduleId
    )).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.get(user, workspaceId, moduleId, challengeId)
    );

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Desafio não encontrado.", exception.getReason());
    verify(moduleRepository, times(1))
          .findByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
          );
    verify(repository, times(1)).findByIdAndModuleId(challengeId, moduleId);
  }

  @Test
  @DisplayName("Deve atualizar um desafio com sucesso")
  void shouldUpdateChallengeSuccessfully() {
    User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
    user.setId(1L);

    Long workspaceId = 10L;

    Long moduleId = 20L;
    Module module = new Module("Módulo", "Descrição");

    Long challengeId = 30L;
    Challenge challenge = new Challenge("Desafio Antigo");

    ChallengeRequest request = new ChallengeRequest("Desafio Novo");

    when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
          moduleId,
          workspaceId,
          user.getId()
    )).thenReturn(Optional.of(module));
    when(repository.findByIdAndModuleId(challengeId, moduleId))
          .thenReturn(Optional.of(challenge));
    when(repository.save(challenge)).thenReturn(challenge);

    ChallengeResponse expectedResponse = new ChallengeResponse(
          challengeId,
          UUID.randomUUID(),
          request.title(),
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
    verify(moduleRepository, times(1))
          .findByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
          );
    verify(repository, times(1)).findByIdAndModuleId(challengeId, moduleId);
    verify(repository, times(1)).save(challenge);
    verify(mapper, times(1)).toResponse(challenge);
  }

  @Test
  @DisplayName("Deve deletar um desafio com sucesso")
  void shouldDeleteChallengeSuccessfully() {
    User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
    user.setId(1L);

    Long workspaceId = 10L;

    Long moduleId = 20L;
    Module module = new Module("Módulo", "Descrição");

    Long challengeId = 30L;

    when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
          moduleId,
          workspaceId,
          user.getId()
    )).thenReturn(Optional.of(module));
    when(repository.deleteByIdAndModuleId(challengeId, moduleId)).thenReturn(1L);

    assertDoesNotThrow(() -> service.delete(
          user,
          workspaceId,
          moduleId,
          challengeId)
    );

    verify(moduleRepository, times(1))
          .findByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
          );
    verify(repository, times(1)).deleteByIdAndModuleId(challengeId, moduleId);
  }

  @Test
  @DisplayName("Deve lançar exceção ao deletar desafio inexistente")
  void shouldThrowExceptionWhenChallengeNotFoundOnDelete() {
    User user = new User("Melqui Martins", "melqui@gmail.com", "123Abcd@");
    user.setId(1L);

    Long workspaceId = 10L;

    Long moduleId = 20L;
    Module module = new Module("Módulo", "Descrição");

    Long challengeId = 30L;

    when(moduleRepository.findByIdAndWorkspaceIdAndWorkspaceUserId(
          moduleId,
          workspaceId,
          user.getId()
    )).thenReturn(Optional.of(module));
    when(repository.deleteByIdAndModuleId(challengeId, moduleId)).thenReturn(0L);

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.delete(user, workspaceId, moduleId, challengeId)
    );

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    verify(moduleRepository, times(1))
          .findByIdAndWorkspaceIdAndWorkspaceUserId(
                moduleId,
                workspaceId,
                user.getId()
          );
    verify(repository, times(1)).deleteByIdAndModuleId(challengeId, moduleId);
  }

}
