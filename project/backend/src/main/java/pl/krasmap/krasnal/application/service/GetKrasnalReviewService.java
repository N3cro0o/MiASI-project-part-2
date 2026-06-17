package pl.krasmap.krasnal.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.krasnal.application.domain.data.KrasnalReview;
import pl.krasmap.krasnal.application.port.out.GetKrasnalReviewInterface;

import java.util.List;

@Service
public class GetKrasnalReviewService {
    private final GetKrasnalReviewInterface reviewPort;

    public GetKrasnalReviewService(GetKrasnalReviewInterface review) {
        reviewPort = review;
    }

    public List<KrasnalReview> GetKrasnalReviews(int krasnalId) {
        return reviewPort.GetAllReviews(krasnalId);
    }
}
