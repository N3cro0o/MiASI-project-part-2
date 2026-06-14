package pl.krasmap.krasnal.infrastructure.out;

import org.springframework.stereotype.Component;
import pl.krasmap.interaction.application.domain.review.Review;
import pl.krasmap.interaction.application.port.in.RequestReviewInterface;
import pl.krasmap.krasnal.application.domain.KrasnalReview;
import pl.krasmap.krasnal.application.domain.KrasnalReviewWeb;
import pl.krasmap.krasnal.application.port.out.GetKrasnalReviewInterface;
import pl.krasmap.krasnal.application.port.out.GetUserInterface;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetKrasnalReviewFromInteraction implements GetKrasnalReviewInterface {
    private final RequestReviewInterface reviewRepo;
    private final GetUserInterface userContext;

    public GetKrasnalReviewFromInteraction(RequestReviewInterface repo, GetUserInterface user) {
        reviewRepo = repo;
        userContext = user;
    }

    @Override
    public List<KrasnalReview> GetAllReviews(int krasnalId) {
        var listToMap = reviewRepo.GetReviewsUnderKrasnal(krasnalId);
        List<KrasnalReview> list = new ArrayList<>();
        for (Review r : listToMap) {
            list.add(KrasnalReview.From(r, userContext.GetLoginFromUser(r.userId())));
        }
        return list;
    }

    @Override
    public KrasnalReview AddReview(int krasnalId, int userId, KrasnalReviewWeb review) {
        Review r = reviewRepo.AddReview(krasnalId, userId, review.rating(), review.content());
        return KrasnalReview.From(r, userContext.GetLoginFromUser(r.userId()));
    }
}
