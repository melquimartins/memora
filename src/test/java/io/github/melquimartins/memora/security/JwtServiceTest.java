package io.github.melquimartins.memora.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

  private JwtService service;

  @BeforeEach
  void setUp() {
    String secret = "my-test-secret-key-that-must-be-long-enough";
    service = new JwtService(secret);
  }

  @Test
  @DisplayName("Deve gerar um token JWT válido com sucesso")
  void shouldGenerateTokenSuccessfully() {
    String subject = "melqui@gmail.com";
    Instant expiration = Instant.now().plus(1, ChronoUnit.HOURS);

    String token = service.generateToken(subject, expiration);

    assertNotNull(token);
    assertFalse(token.isEmpty());
  }

  @Test
  @DisplayName("Deve validar um token JWT e extrair o subject com sucesso")
  void shouldValidateTokenAndReturnSubjectSuccessfully() {
    String subject = "melqui@gmail.com";
    Instant expiration = Instant.now().plus(1, ChronoUnit.HOURS);
    String token = service.generateToken(subject, expiration);

    String validatedSubject = service.validateToken(token);

    assertNotNull(validatedSubject);
    assertEquals(subject, validatedSubject);
  }

  @Test
  @DisplayName("Deve lançar exceção ao validar um token inválido")
  void shouldThrowExceptionWhenTokenIsInvalid() {
    String invalidToken = "invalid.token.value";

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.validateToken(invalidToken)
    );

    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    assertEquals(
          "O token de autenticação é inválido ou expirou. Faça login novamente."
          , exception.getReason()
    );
  }

  @Test
  @DisplayName("Deve lançar exceção ao validar um token expirado")
  void shouldThrowExceptionWhenTokenIsExpired() {
    String subject = "melqui@gmail.com";
    Instant expiration = Instant.now().minus(1, ChronoUnit.HOURS);
    String expiredToken = service.generateToken(subject, expiration);

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.validateToken(expiredToken)
    );

    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
  }

}
