package pl.krasmap.interaction.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.krasmap.interaction.application.domain.data.review.Review;
import pl.krasmap.interaction.application.domain.data.review.ReviewWeb;
import pl.krasmap.interaction.application.domain.event.review.ReviewAddedEvent;
import pl.krasmap.interaction.application.domain.event.review.ReviewDeleteEvent;
import pl.krasmap.interaction.application.domain.event.review.ReviewUpdatedEvent;

import java.util.List;

@Service
public class HandleReviewService {

    private final HoldReviewRepo repos;
    private final ApplicationEventPublisher events;

    public HandleReviewService(HoldReviewRepo r, ApplicationEventPublisher e){
        repos = r;
        events = e;
    }

    public List<Review> GetReviewsUnderKrasnal(int krasnalId) {
        return repos.GetReviewsUnderKrasnal(krasnalId);
    }

    public Review GetReviewById(int reviewId) {
        return repos.GetReviewById(reviewId);
    }

    public List<Review> GetReviewsFromUser(int userId) {
        return repos.GetReviewsFromUser(userId);
    }

    public List<Review> GetReviewsFromUserUnderKrasnal(int userId, int krasnalId) {
        return repos.GetReviewsFromUserUnderKrasnal(userId, krasnalId);
    }

    public Review AddReview(ReviewWeb reviewToAdd) {
        int id = repos.AddReview(reviewToAdd);
        events.publishEvent(new ReviewAddedEvent(id, reviewToAdd.krasnalId(), reviewToAdd.userId()));
        return GetReviewById(id);
    }

    public Review UpdateReview(int reviewId, ReviewWeb reviewToUpdate) {
        repos.UpdateReview(reviewId, reviewToUpdate);
        events.publishEvent(new ReviewUpdatedEvent(reviewId, reviewToUpdate.krasnalId(), reviewToUpdate.userId()));
        return GetReviewById(reviewId);
    }

    public boolean RemoveReview(int reviewId) {
        Review r = GetReviewById(reviewId);
        boolean b = repos.RemoveReview(reviewId);
        events.publishEvent(new ReviewDeleteEvent(reviewId, r.krasnalId(), r.userId()));
        return b;
    }
}
