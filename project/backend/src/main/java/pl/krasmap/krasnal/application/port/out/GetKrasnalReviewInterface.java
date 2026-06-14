package pl.krasmap.krasnal.application.port.out;

import pl.krasmap.krasnal.application.domain.KrasnalReview;
import pl.krasmap.krasnal.application.domain.KrasnalReviewWeb;

import java.util.List;

public interface GetKrasnalReviewInterface {
    List<KrasnalReview> GetAllReviews(int krasnalId);
    KrasnalReview AddReview(int krasnalId, int userId, KrasnalReviewWeb review);
}
