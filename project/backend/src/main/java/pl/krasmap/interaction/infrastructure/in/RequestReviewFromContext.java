package pl.krasmap.interaction.infrastructure.in;

import org.springframework.stereotype.Component;
import pl.krasmap.interaction.application.domain.review.Review;
import pl.krasmap.interaction.application.domain.review.ReviewWeb;
import pl.krasmap.interaction.application.port.in.RequestReviewInterface;
import pl.krasmap.interaction.application.service.HoldReviewRepo;
import pl.krasmap.krasnal.application.domain.KrasnalReviewWeb;
import pl.krasmap.krasnal.application.domain.KrasnalWeb;

import java.util.List;

@Component
public class RequestReviewFromContext implements RequestReviewInterface {

    private final HoldReviewRepo reviewRepo;

    public RequestReviewFromContext(HoldReviewRepo repo) {
        reviewRepo = repo;
    }

    @Override
    public List<Review> GetReviewsUnderKrasnal(int krasnalId) {
        return reviewRepo.GetReviewsUnderKrasnal(krasnalId);
    }

    @Override
    public List<Review> GetReviewsFromUser(int userId) {
        return reviewRepo.GetReviewsFromUser(userId);
    }

    @Override
    public Review AddReview(int krasnalId, int userId, short rating, String content) {
        ReviewWeb k = new ReviewWeb(krasnalId, userId, rating, content);
        return reviewRepo.AddReview(k);
    }
}
