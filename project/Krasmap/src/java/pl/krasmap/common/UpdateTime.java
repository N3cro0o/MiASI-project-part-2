package pl.krasmap.common;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UpdateTime(LocalDateTime created, LocalDateTime updated) {

    public static UpdateTime now() {
        return new UpdateTime(LocalDateTime.now(), LocalDateTime.now());
    }
}
