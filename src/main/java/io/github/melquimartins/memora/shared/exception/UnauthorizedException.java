package io.github.melquimartins.memora.shared.exception;

public class UnauthorizedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Você não tem permissão para acessar este recurso.";

    public UnauthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public UnauthorizedException(String message) {
        super(message);
    }

}
