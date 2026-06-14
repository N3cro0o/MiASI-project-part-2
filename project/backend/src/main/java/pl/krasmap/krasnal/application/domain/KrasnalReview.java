package pl.krasmap.krasnal.application.domain;

import pl.krasmap.interaction.application.domain.review.Review;

import java.time.OffsetDateTime;

public record KrasnalReview(int userId, short rating, String content, OffsetDateTime created) {

    public static KrasnalReview From(Review r) {
        return new KrasnalReview(r.userId(), r.rating(), r.content(), r.created());
    }
}
