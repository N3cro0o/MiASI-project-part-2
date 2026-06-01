package pl.krasmap.krasnal.application.domain;

import java.time.LocalDateTime;

public record KrasnalReview(int id, int userId, short rating, String content, LocalDateTime created) {
}
