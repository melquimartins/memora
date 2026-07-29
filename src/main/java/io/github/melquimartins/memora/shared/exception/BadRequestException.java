package io.github.melquimartins.memora.shared.exception;

public class BadRequestException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Requisição inválida.";

    public BadRequestException() {
        super(DEFAULT_MESSAGE);
    }

    public BadRequestException(String message) {
        super(message);
    }
}
