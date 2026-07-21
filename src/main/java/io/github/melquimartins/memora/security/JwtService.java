package io.github.melquimartins.memora.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class JwtService {

  private final Algorithm algorithm;

  public JwtService(@Value("${jwt.secret}") String secret) {
    this.algorithm = Algorithm.HMAC256(secret);
  }

  public String generateToken(String subject, Instant expirationDate) {
    try {
      return JWT
            .create()
            .withIssuer("memora")
            .withSubject(subject)
            .withExpiresAt(expirationDate)
            .sign(algorithm);
    } catch (JWTCreationException e) {
      throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Não foi possível gerar o token de autenticação. Tente novamente mais tarde."
      );
    }
  }

  public String validateToken(String token) {
    try {
      return JWT
            .require(algorithm)
            .withIssuer("memora")
            .build()
            .verify(token)
            .getSubject();
    } catch (JWTVerificationException e) {
      throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "O token de autenticação é inválido ou expirou. Faça login novamente."
      );
    }
  }

}
