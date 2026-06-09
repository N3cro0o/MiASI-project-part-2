package pl.krasmap.krasnal.infrastructure.out;

import org.springframework.stereotype.Component;
import pl.krasmap.interaction.application.domain.Review;
import pl.krasmap.interaction.application.service.HoldReviewRepo;
import pl.krasmap.krasnal.application.domain.KrasnalReview;
import pl.krasmap.krasnal.application.port.out.GetKrasnalReviewInterface;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetKrasnalReviewFromInteraction implements GetKrasnalReviewInterface {
    private final HoldReviewRepo reviewRepo;

    public GetKrasnalReviewFromInteraction(HoldReviewRepo repo) {
        reviewRepo = repo;
    }

    @Override
    public List<KrasnalReview> GetAllReviews(int krasnalId) {
        var listToMap = reviewRepo.getReviewList(krasnalId);
        List<KrasnalReview> list = new ArrayList<>();
        for (Review r : listToMap) {
            list.add(new KrasnalReview(r.id(), r.userId(), r.rating(), r.content(), r.created()));
        }
        return list;
    }
}
