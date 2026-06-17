package pl.krasmap.interaction.infrastructure.in;

import org.springframework.stereotype.Component;
import pl.krasmap.interaction.application.domain.data.review.Review;
import pl.krasmap.interaction.application.domain.data.review.ReviewWeb;
import pl.krasmap.interaction.application.port.in.RequestReviewInterface;
import pl.krasmap.interaction.application.service.HandleReviewService;
import pl.krasmap.interaction.application.service.HoldReviewRepo;

import java.util.List;

@Component
public class RequestReviewFromContext implements RequestReviewInterface {

    private final HandleReviewService reviewHandle;

    public RequestReviewFromContext(HandleReviewService repo) {
        reviewHandle = repo;
    }

    @Override
    public List<Review> GetReviewsUnderKrasnal(int krasnalId) {
        return reviewHandle.GetReviewsUnderKrasnal(krasnalId);
    }

    @Override
    public List<Review> GetReviewsFromUser(int userId) {
        return reviewHandle.GetReviewsFromUser(userId);
    }

    @Override
    public Review AddReview(int krasnalId, int userId, short rating, String content) {
        ReviewWeb k = new ReviewWeb(krasnalId, userId, rating, content);
        return reviewHandle.AddReview(k);
    }
}
