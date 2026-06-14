package pl.krasmap.interaction.application.port.in;

import pl.krasmap.interaction.application.domain.review.Review;

import java.util.List;

public interface RequestReviewInterface {
    List<Review> GetReviewsUnderKrasnal(int krasnalId);
    List<Review> GetReviewsFromUser(int userId);
    Review AddReview(int krasnalId, int userId, short rating, String content);
}
