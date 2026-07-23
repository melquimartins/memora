package io.github.melquimartins.memora.shared.dto;

public record ResponseEnvelope<T>(String message, T data) {
}
