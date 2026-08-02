package io.github.melquimartins.memora.shared.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    private static final String DEFAULT_MESSAGE = "Você não tem permissão para acessar este recurso.";

    public UnauthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public UnauthorizedException(String message) {
        super(message);
    }

}
