package io.github.melquimartins.memora.domain.challenge.enums;

import io.github.melquimartins.memora.domain.challenge.valueobject.DifficultyInterval;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

public enum DifficultyLevel {

    EASY(new DifficultyInterval(1, ChronoUnit.DAYS)),
    MEDIUM(new DifficultyInterval(1, ChronoUnit.HOURS)),
    HARD(new DifficultyInterval(10, ChronoUnit.MINUTES));

    private final DifficultyInterval difficultyInterval;

    DifficultyLevel(DifficultyInterval difficultyInterval) {
        this.difficultyInterval = difficultyInterval;
    }

    public DifficultyInterval getDifficultyInterval() {
        return difficultyInterval;
    }

}
