package pl.krasmap.submission.application.service;

import org.springframework.stereotype.Repository;
import pl.krasmap.submission.application.domain.data.NewSubmission;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.port.out.SubmissionFetchInterface;

import java.util.List;

@Repository
public class HoldSubmissionRepo {
    private final SubmissionFetchInterface subFetch;

    public HoldSubmissionRepo(SubmissionFetchInterface fetch) {
        subFetch = fetch;
    }

    public int AddSubmission(NewSubmission submission) {
        return subFetch.AddSubmission(submission);
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

    public boolean UpdateSubReview(Submission newSub) {
        return subFetch.UpdateSubReview(newSub);
    }

    public int UpdateSubmission(int subId, NewSubmission submission) {
        return subFetch.UpdateSubmission(subId, submission);
    }

    public List<Submission> GetAllSubmissions() {
        return subFetch.GetAllSubmissions();
    }

    public List<Submission> GetAllSubmissions(SubmissionStatus status) {
        return subFetch.GetAllSubmissions(status);
    }
}
