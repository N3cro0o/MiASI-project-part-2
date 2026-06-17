package pl.krasmap.krasnal.application.domain.data;

import pl.krasmap.interaction.application.domain.data.review.Review;

import java.time.OffsetDateTime;

public record KrasnalReview(String login, short rating, String content, OffsetDateTime created) {

    public static KrasnalReview From(Review r, String login) {
        return new KrasnalReview(login, r.rating(), r.content(), r.created());
    }
}
