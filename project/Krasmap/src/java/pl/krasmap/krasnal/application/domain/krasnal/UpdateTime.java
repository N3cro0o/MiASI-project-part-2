package pl.krasmap.krasnal.application.domain.krasnal;

import java.time.OffsetDateTime;

public record UpdateTime(OffsetDateTime created, OffsetDateTime updated) {

    public static UpdateTime now() {
        return new UpdateTime(OffsetDateTime.now(), OffsetDateTime.now());
    }

    public static UpdateTime from(OffsetDateTime created, OffsetDateTime updated) {
        return new UpdateTime(created, updated);
    }
}
