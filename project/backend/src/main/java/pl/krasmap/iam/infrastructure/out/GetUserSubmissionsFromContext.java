package pl.krasmap.iam.infrastructure.out;

import org.springframework.stereotype.Component;
import pl.krasmap.iam.application.domain.stats.UserSubmission;
import pl.krasmap.iam.application.port.out.GetUserSubmissionsInterface;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.port.in.RequestSubmissionsInterface;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetUserSubmissionsFromContext implements GetUserSubmissionsInterface {

    private final RequestSubmissionsInterface subContext;

    public GetUserSubmissionsFromContext(RequestSubmissionsInterface c){
        subContext = c;
    }

    @Override
    public List<UserSubmission> GetUserSubmissions(int userId) {
        List<UserSubmission> l = new ArrayList<>();
        for (Submission s : subContext.GetUserSubmissions(userId)){
            l.add(UserSubmission.From(s));
        }
        return l;
    }
}
