package pl.krasmap.interaction.infrastructure.in;

import org.springframework.stereotype.Component;
import pl.krasmap.interaction.application.domain.review.Review;
import pl.krasmap.interaction.application.port.in.RequestReviewInterface;
import pl.krasmap.interaction.application.service.HoldReviewRepo;

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
}
