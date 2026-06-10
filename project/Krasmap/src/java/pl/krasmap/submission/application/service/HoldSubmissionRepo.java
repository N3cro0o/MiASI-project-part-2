package pl.krasmap.submission.application.service;

import org.springframework.stereotype.Repository;
import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.domain.submission.SubmissionStatus;
import pl.krasmap.submission.application.port.out.SubmissionFetchInterface;

import java.util.List;

@Repository
public class HoldSubmissionRepo {
    private final SubmissionFetchInterface subFetch;

    public HoldSubmissionRepo(SubmissionFetchInterface fetch) {
        subFetch = fetch;
    }

    public Submission AddSubmission(NewSubmission submission) {
        int id = subFetch.AddSubmission(submission);
        return subFetch.GetSubmission(id);
    }

    public SubmissionStatus CheckSubmission(int subId) {
        return subFetch.CheckSubmission(subId);
    }

    public List<Submission> GetSubmissionsFromUser(int userId) {
        return subFetch.GetSubmissionsFromUser(userId);
    }

    public Submission GetSubmission(int subId) {
        return subFetch.GetSubmission(subId);
    }
}
