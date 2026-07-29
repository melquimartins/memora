package io.github.melquimartins.memora.shared.exception;

public class InternalServerErrorException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Erro interno do servidor.";

    public InternalServerErrorException() {
        super(DEFAULT_MESSAGE);
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

}
