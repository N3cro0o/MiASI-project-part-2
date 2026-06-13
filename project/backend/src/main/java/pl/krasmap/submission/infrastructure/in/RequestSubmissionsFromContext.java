package pl.krasmap.submission.infrastructure.in;

import org.springframework.stereotype.Service;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.port.in.RequestSubmissionsInterface;
import pl.krasmap.submission.application.service.HoldSubmissionRepo;

import java.util.List;

@Service
public class RequestSubmissionsFromContext implements RequestSubmissionsInterface {

    private final HoldSubmissionRepo subRepo;

    public RequestSubmissionsFromContext(HoldSubmissionRepo repo){
        subRepo = repo;
    }

    @Override
    public List<Submission> GetUserSubmissions(int userId) {
        return subRepo.GetSubmissionsFromUser(userId);
    }
}
