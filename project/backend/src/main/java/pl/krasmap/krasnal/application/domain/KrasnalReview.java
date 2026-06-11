package pl.krasmap.krasnal.application.domain;

import java.time.OffsetDateTime;

public record KrasnalReview(int id, int userId, short rating, String content, OffsetDateTime created) {
}
