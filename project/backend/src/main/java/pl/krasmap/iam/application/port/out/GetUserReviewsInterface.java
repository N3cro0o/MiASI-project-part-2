package pl.krasmap.iam.application.port.out;

import pl.krasmap.iam.application.domain.stats.UserReview;

import java.util.List;

public interface GetUserReviewsInterface {
    List<UserReview> GetUserReviews(int userId);
}
