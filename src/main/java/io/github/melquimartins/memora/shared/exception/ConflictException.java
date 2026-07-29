package io.github.melquimartins.memora.shared.exception;

public class ConflictException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Requisição conflita com o estado atual do servidor.";

    public ConflictException() {
        super(DEFAULT_MESSAGE);
    }

    public ConflictException(String message) {
        super(message);
    }

}
