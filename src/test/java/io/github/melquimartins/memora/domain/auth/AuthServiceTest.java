package io.github.melquimartins.memora.domain.auth;

import io.github.melquimartins.memora.domain.auth.dto.SignInRequest;
import io.github.melquimartins.memora.domain.auth.dto.SignUpRequest;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.domain.user.UserRepository;
import io.github.melquimartins.memora.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private JwtService jwtService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserRepository repository;

  @InjectMocks
  private AuthService service;

  @Test
  @DisplayName("Deve fazer login com sucesso e retornar token")
  void shouldSignInSuccessfully() {
    SignInRequest request = new SignInRequest("usuario@email.com", "senha123");
    User user = new User(
          "Nome do Usuário",
          request.email(),
          "senhaCriptografada"
    );

    when(repository.findByEmail(request.email())).thenReturn(Optional.of(user));
    when(jwtService.generateToken(eq(request.email()), any(Instant.class)))
          .thenReturn("token-jwt-valido");

    String token = service.signIn(request);

    assertNotNull(token);
    assertEquals("token-jwt-valido", token);
    verify(repository, times(1)).findByEmail(request.email());
    verify(authenticationManager, times(1))
          .authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtService, times(1))
          .generateToken(eq(request.email()), any(Instant.class));
  }

  @Test
  @DisplayName("Deve lançar exceção ao fazer login com e-mail não cadastrado")
  void shouldThrowExceptionWhenUserEmailNotFoundOnSignIn() {
    SignInRequest request = new SignInRequest("usuario@email.com", "senha123");

    when(repository.findByEmail(request.email())).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.signIn(request)
    );

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals(
          "E-mail ou senha incorretos. Verifique " +
                "suas credenciais e tente novamente.",
          exception.getReason()
    );
    verify(repository, times(1)).findByEmail(request.email());
    verifyNoInteractions(authenticationManager);
    verifyNoInteractions(jwtService);
  }

  @Test
  @DisplayName("Deve registrar um novo usuário com sucesso e retornar token")
  void shouldSignUpSuccessfully() {
    SignUpRequest request = new SignUpRequest(
          "Nome do Usuário",
          "usuario@email.com",
          "senha123"
    );

    when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(request.password()))
          .thenReturn("senhaCriptografada");
    when(jwtService.generateToken(eq(request.email()), any(Instant.class)))
          .thenReturn("token-jwt-valido");

    String token = service.signUp(request);

    assertNotNull(token);
    assertEquals("token-jwt-valido", token);
    verify(repository, times(1)).findByEmail(request.email());
    verify(passwordEncoder, times(1)).encode(request.password());
    verify(repository, times(1)).save(any(User.class));
    verify(jwtService, times(1))
          .generateToken(eq(request.email()), any(Instant.class));
  }

  @Test
  @DisplayName("Deve lançar exceção ao registrar usuário com e-mail já existente")
  void shouldThrowExceptionWhenUserEmailAlreadyExistsOnSignUp() {
    SignUpRequest request = new SignUpRequest(
          "Nome do Usuário",
          "usuario@email.com",
          "senha123"
    );
    User existingUser = new User(
          "Nome do Usuário",
          request.email(),
          "senhaCriptografada"
    );

    when(repository.findByEmail(request.email()))
          .thenReturn(Optional.of(existingUser));

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.signUp(request)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
          "Este e-mail já está vinculado a uma conta. " +
                "Tente fazer login ou use outro e-mail.",
          exception.getReason()
    );
    verify(repository, times(1)).findByEmail(request.email());
    verify(passwordEncoder, never()).encode(any());
    verify(repository, never()).save(any());
    verifyNoInteractions(jwtService);
  }
}
