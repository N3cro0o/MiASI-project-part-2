package pl.krasmap.interaction.application.domain;

import java.time.OffsetDateTime;

public record Review(int id, int krasnalId, int userId, short rating, String content, OffsetDateTime created) {

    public static Review newObject(int id, int krasnalId, int userId, short rating, String content, OffsetDateTime created) {
        return new Review(id, krasnalId, userId, rating, content, created);
    }
}
