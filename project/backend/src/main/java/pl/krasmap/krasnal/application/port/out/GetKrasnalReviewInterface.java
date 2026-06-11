package pl.krasmap.krasnal.application.port.out;

import pl.krasmap.krasnal.application.domain.KrasnalReview;

import java.util.List;

public interface GetKrasnalReviewInterface {
    List<KrasnalReview> GetAllReviews(int krasnalId);
}
