package pl.krasmap.common.data;

import java.time.OffsetDateTime;

public record UpdateTime(OffsetDateTime created, OffsetDateTime updated) {

    public static UpdateTime now() {
        OffsetDateTime now = OffsetDateTime.now();
        return new UpdateTime(now, now);
    }

    public static UpdateTime from(OffsetDateTime created, OffsetDateTime updated) {
        return new UpdateTime(created, updated);
    }
}
