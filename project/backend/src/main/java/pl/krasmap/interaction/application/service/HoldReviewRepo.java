package pl.krasmap.interaction.application.service;

import org.springframework.stereotype.Repository;
import pl.krasmap.interaction.application.domain.review.Review;
import pl.krasmap.interaction.application.domain.review.ReviewWeb;
import pl.krasmap.interaction.application.port.out.ReviewFetchInterface;

import java.util.List;

@Repository
public class HoldReviewRepo {
    private final ReviewFetchInterface reviewFetch;

    public HoldReviewRepo(ReviewFetchInterface fetch){
        reviewFetch = fetch;
    }

    public List<Review> GetReviewsUnderKrasnal(int krasnalId) {
        return reviewFetch.GetReviewsUnderKrasnal(krasnalId);
    }

    public Review GetReviewById(int reviewId) {
        return reviewFetch.GetReview(reviewId);
    }

    public List<Review> GetReviewsFromUser(int userId) {
        return reviewFetch.GetReviewsFromUser(userId);
    }

    public List<Review> GetReviewsFromUserUnderKrasnal(int userId, int krasnalId) {
        return reviewFetch.GetReviewsFromUserUnderKrasnal(userId, krasnalId);
    }

    public Review AddReview(ReviewWeb reviewToAdd) {
        int id = reviewFetch.AddReview(reviewToAdd);
        return reviewFetch.GetReview(id);
    }

    public Review UpdateReview(int reviewId, ReviewWeb reviewToUpdate) {
        int id = reviewFetch.UpdateReview(reviewId, reviewToUpdate);
        return reviewFetch.GetReview(id);
    }

    public boolean RemoveReview(int reviewId) {
        return reviewFetch.RemoveReview(reviewId);
    }
}
