package pl.krasmap.submission.application.port.in;

import org.apache.commons.lang3.tuple.Pair;
import pl.krasmap.submission.application.domain.ReviewKrasnal;
import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.common.data.SubmissionStatus;

import java.util.List;

public interface SubmissionControllerInterface {
    Submission PostSubmission(NewSubmission submission);
    SubmissionStatus CheckSubmission(int subId);
    List<Submission> GetSubmissionsFromUser(int userId);
    Pair<Submission, ReviewKrasnal> GetSubmission(int subId);
    Boolean RejectSubmission(int userId, int subId, String reason);
    ReviewKrasnal AcceptSubmission(int userId, int subId);
    Submission UpdateSubmission(int subId, NewSubmission submission);
    List<Submission> GetAllSubmissions();
    List<Submission> GetAllSubmissions(SubmissionStatus status);
    Boolean CanAcceptSubmission(int userId, int subId);
}
