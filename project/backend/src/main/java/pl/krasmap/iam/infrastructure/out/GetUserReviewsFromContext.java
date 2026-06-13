package pl.krasmap.iam.infrastructure.out;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.krasmap.iam.application.domain.UserSubmission;
import pl.krasmap.iam.application.port.out.GetUserReviewsInterface;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.port.in.RequestSubmissionsInterface;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetUserReviewsFromContext implements GetUserReviewsInterface {

    private final RequestSubmissionsInterface subRequest;

    public GetUserReviewsFromContext(@Lazy RequestSubmissionsInterface request){
        subRequest = request;
    }

    @Override
    public List<UserSubmission> GetUserSubmissions(int userId) {
        List<UserSubmission> l = new ArrayList<>();
        List<Submission> list = subRequest.GetUserSubmissions(userId);
        for (Submission s : list){
            l.add(UserSubmission.From(s));
        }
        return l;
    }
}
