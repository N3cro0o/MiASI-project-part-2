package pl.krasmap.iam.infrastructure.out;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.krasmap.iam.application.domain.data.stats.UserSubmission;
import pl.krasmap.iam.application.port.out.GetUserSubmissionsInterface;
import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.submission.application.port.in.RequestSubmissionsInterface;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetUserSubmissionsFromContext implements GetUserSubmissionsInterface {

    private final RequestSubmissionsInterface subContext;

    public GetUserSubmissionsFromContext(@Lazy RequestSubmissionsInterface c){
        subContext = c;
    }

    @Override
    public List<UserSubmission> GetUserSubmissions(int userId) {
        List<UserSubmission> l = new ArrayList<>();
        for (Submission s : subContext.GetUserSubmissions(userId)){
            ReviewKrasnal k = subContext.ParseKrasnalFromJson(s.json());
            l.add(UserSubmission.From(s, k));
        }
        return l;
    }
}
