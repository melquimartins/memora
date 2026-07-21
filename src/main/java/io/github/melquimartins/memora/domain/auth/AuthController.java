package io.github.melquimartins.memora.domain.auth;

import io.github.melquimartins.memora.domain.auth.dto.SignInRequest;
import io.github.melquimartins.memora.domain.auth.dto.SignUpRequest;
import io.github.melquimartins.memora.domain.auth.normalizer.SignUpRequestNormalizer;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService service;
  private final SignUpRequestNormalizer normalizer;

  public AuthController(
        AuthService service,
        SignUpRequestNormalizer normalizer
  ) {
    this.service = service;
    this.normalizer = normalizer;
  }

  @PostMapping("/sign-in")
  public ResponseEntity<String> signIn(
        @Valid @RequestBody SignInRequest request
  ) {
    String token = service.signIn(request);

    ResponseCookie cookie = createTokenCookie(token);

    return ResponseEntity
          .ok()
          .header(HttpHeaders.SET_COOKIE, cookie.toString())
          .body("Bem-vindo de volta! Login realizado com sucesso.");
  }

  @PostMapping("/sign-up")
  public ResponseEntity<String> signUp(
        @Valid @RequestBody SignUpRequest request
  ) {
    SignUpRequest normalized = normalizer.normalize(request);
    String token = service.signUp(normalized);

    ResponseCookie cookie = createTokenCookie(token);

    return ResponseEntity
          .status(HttpStatus.CREATED)
          .header(HttpHeaders.SET_COOKIE, cookie.toString())
          .body("Conta criada com sucesso! Bem-vindo ao Memora.");
  }

  private ResponseCookie createTokenCookie(String token) {
    return ResponseCookie.from("token", token)
          .httpOnly(true)
          .secure(false)
          .path("/")
          .maxAge((int) Duration.ofDays(7).toSeconds())
          .build();
  }

}
