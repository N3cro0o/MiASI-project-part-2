package pl.krasmap.interaction.application.service;

import org.springframework.stereotype.Repository;
import pl.krasmap.interaction.application.domain.data.review.Review;
import pl.krasmap.interaction.application.domain.data.review.ReviewWeb;
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

    public int AddReview(ReviewWeb reviewToAdd) {
        return reviewFetch.AddReview(reviewToAdd);
    }

    public int UpdateReview(int reviewId, ReviewWeb reviewToUpdate) {
        return reviewFetch.UpdateReview(reviewId, reviewToUpdate);
    }

    public boolean RemoveReview(int reviewId) {
        return reviewFetch.RemoveReview(reviewId);
    }
}
