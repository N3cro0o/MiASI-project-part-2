package pl.krasmap.interaction.application.port.in;

import pl.krasmap.interaction.application.domain.Review;
import pl.krasmap.interaction.application.domain.ReviewWeb;

import java.util.List;

public interface ReviewControllerInterface {
    Review GetReview(int reviewId);
    List<Review> GetReviewsUnderKrasnal(int krasnalId);
    List<Review> GetReviewsFromUser(int userId);
    List<Review> GetReviewsFromUserUnderKrasnal(int userId, int krasnalId);
    Review AddReview(ReviewWeb reviewToAdd);
    Review UpdateReview(int reviewId, ReviewWeb reviewToUpdate);
    Boolean RemoveReview(int reviewId);
}
