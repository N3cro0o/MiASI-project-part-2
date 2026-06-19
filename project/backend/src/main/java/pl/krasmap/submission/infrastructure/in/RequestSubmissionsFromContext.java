package pl.krasmap.submission.infrastructure.in;

import org.springframework.stereotype.Service;
import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.submission.application.port.in.RequestSubmissionsInterface;
import pl.krasmap.submission.application.domain.service.CheckSubmission;
import pl.krasmap.submission.application.service.HandleSubmissionService;
import pl.krasmap.submission.application.service.HoldSubmissionRepo;

import java.util.List;

@Service
public class RequestSubmissionsFromContext implements RequestSubmissionsInterface {

    private final HandleSubmissionService subHandle;

    public RequestSubmissionsFromContext(HandleSubmissionService repo){
        subHandle = repo;
    }

    @Override
    public List<Submission> GetUserSubmissions(int userId) {
        return subHandle.GetSubmissionsFromUser(userId);
    }

    @Override
    public ReviewKrasnal ParseKrasnalFromJson(String json) {
        return CheckSubmission.GenerateKrasnalFromJson(json);
    }
}
