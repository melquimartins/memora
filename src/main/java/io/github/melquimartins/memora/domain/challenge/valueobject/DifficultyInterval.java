package io.github.melquimartins.memora.domain.challenge.valueobject;

import java.time.temporal.ChronoUnit;

public record DifficultyInterval(int amount, ChronoUnit unit) {
}
