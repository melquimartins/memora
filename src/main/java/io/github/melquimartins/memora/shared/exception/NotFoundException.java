package io.github.melquimartins.memora.shared.exception;

public class NotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Recurso não encontrado.";

    public NotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public NotFoundException(String message) {
        super(message);
    }

}
