package pl.krasmap.iam.infrastructure.out;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.krasmap.iam.application.domain.stats.UserReview;
import pl.krasmap.iam.application.port.out.GetUserReviewsInterface;
import pl.krasmap.interaction.application.domain.review.Review;
import pl.krasmap.interaction.application.port.in.RequestReviewInterface;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetUserReviewsFromContext implements GetUserReviewsInterface {

    private final RequestReviewInterface revRequest;

    public GetUserReviewsFromContext(@Lazy RequestReviewInterface request){
        revRequest = request;
    }

    @Override
    public List<UserReview> GetUserReviews(int userId) {
        List<UserReview> l = new ArrayList<>();
        List<Review> list = revRequest.GetReviewsFromUser(userId);
        for (Review s : list){
            l.add(UserReview.From(s));
        }
        return l;
    }
}
