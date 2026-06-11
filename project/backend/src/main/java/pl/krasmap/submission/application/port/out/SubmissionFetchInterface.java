package pl.krasmap.submission.application.port.out;

import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.domain.submission.SubmissionStatus;

import java.util.List;

public interface SubmissionFetchInterface {
    int AddSubmission(NewSubmission sub);
    Submission GetSubmission(int subId);
    SubmissionStatus CheckSubmission(int subId);
    List<Submission> GetSubmissionsFromUser(int userId);
    boolean UpdateSubReview(Submission newSub);
    int UpdateSubmission(int subId, NewSubmission submission);
}
