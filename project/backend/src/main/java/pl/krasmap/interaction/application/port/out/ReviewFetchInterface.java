package pl.krasmap.interaction.application.port.out;

import pl.krasmap.interaction.application.domain.review.Review;
import pl.krasmap.interaction.application.domain.review.ReviewWeb;

import java.util.List;

public interface ReviewFetchInterface {
    Review GetReview(int reviewId);
    List<Review> GetReviewsUnderKrasnal(int krasnalId);
    List<Review> GetReviewsFromUser(int userId);
    List<Review> GetReviewsFromUserUnderKrasnal(int userId, int krasnalId);
    int AddReview(ReviewWeb reviewToAdd);
    int UpdateReview(int reviewId, ReviewWeb reviewToUpdate);
    boolean RemoveReview(int reviewId);
}
