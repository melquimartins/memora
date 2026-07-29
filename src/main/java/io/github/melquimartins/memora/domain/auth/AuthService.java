package io.github.melquimartins.memora.domain.auth;

import io.github.melquimartins.memora.domain.auth.dto.SignInRequest;
import io.github.melquimartins.memora.domain.auth.dto.SignUpRequest;
import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.domain.user.UserRepository;
import io.github.melquimartins.memora.security.JwtService;
import io.github.melquimartins.memora.shared.exception.ConflictException;
import io.github.melquimartins.memora.shared.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;

    public AuthService(
            JwtService jwtService, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserRepository repository
    ) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.repository = repository;
    }

    public String signIn(SignInRequest request) {
        repository
                .findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException(
                        "E-mail ou senha incorretos. Verifique suas credenciais e tente novamente"));

        var authenticationToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );

        authenticationManager.authenticate(authenticationToken);

        return jwtService.generateToken(request.email(), generateExpirationTime());
    }

    public String signUp(SignUpRequest request) {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException(
                    "Este e-mail já está vinculado a uma conta. Tente fazer login ou use outro e-mail."
            );
        }

        String encryptedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.name(),
                request.email(),
                encryptedPassword
        );

        repository.save(user);

        return jwtService.generateToken(request.email(), generateExpirationTime());
    }

    private Instant generateExpirationTime() {
        return Instant.now().plus(7, ChronoUnit.DAYS);
    }

}
