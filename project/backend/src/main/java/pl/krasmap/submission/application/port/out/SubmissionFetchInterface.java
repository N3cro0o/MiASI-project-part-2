package pl.krasmap.submission.application.port.out;

import pl.krasmap.submission.application.domain.data.NewSubmission;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.common.data.SubmissionStatus;

import java.util.List;

public interface SubmissionFetchInterface {
    int AddSubmission(NewSubmission sub);
    Submission GetSubmission(int subId);
    SubmissionStatus CheckSubmission(int subId);
    List<Submission> GetSubmissionsFromUser(int userId);
    boolean UpdateSubReview(Submission newSub);
    int UpdateSubmission(int subId, NewSubmission submission);
    List<Submission> GetAllSubmissions();
    List<Submission> GetAllSubmissions(SubmissionStatus status);
}
