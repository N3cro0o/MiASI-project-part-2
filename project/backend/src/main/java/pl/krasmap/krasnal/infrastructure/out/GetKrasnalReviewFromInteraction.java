package pl.krasmap.krasnal.infrastructure.out;

import org.springframework.stereotype.Component;
import pl.krasmap.interaction.application.domain.review.Review;
import pl.krasmap.interaction.application.port.in.RequestReviewInterface;
import pl.krasmap.krasnal.application.domain.KrasnalReview;
import pl.krasmap.krasnal.application.port.out.GetKrasnalReviewInterface;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetKrasnalReviewFromInteraction implements GetKrasnalReviewInterface {
    private final RequestReviewInterface reviewRepo;

    public GetKrasnalReviewFromInteraction(RequestReviewInterface repo) {
        reviewRepo = repo;
    }

    @Override
    public List<KrasnalReview> GetAllReviews(int krasnalId) {
        var listToMap = reviewRepo.GetReviewsUnderKrasnal(krasnalId);
        List<KrasnalReview> list = new ArrayList<>();
        for (Review r : listToMap) {
            list.add(new KrasnalReview(r.userId(), r.rating(), r.content(), r.created()));
        }
        return list;
    }
}
