package pl.krasmap.iam.application.domain.data.stats;

import pl.krasmap.interaction.application.domain.data.review.Review;

import java.time.OffsetDateTime;

public record UserReview(int krasnalId, short rating, String content, OffsetDateTime created) {

    public static UserReview From(Review s) {
        return new UserReview(s.krasnalId(), s.rating(), s.content(), s.created());
    }
}
