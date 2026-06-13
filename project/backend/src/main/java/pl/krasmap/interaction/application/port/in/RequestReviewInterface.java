package pl.krasmap.interaction.application.port.in;

import pl.krasmap.interaction.application.domain.Review;

import java.util.List;

public interface RequestReviewInterface {
    List<Review> GetReviewsUnderKrasnal(int krasnalId);
    List<Review> GetReviewsFromUser(int userId);
}
