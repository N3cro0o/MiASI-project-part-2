package pl.krasmap.submission.application.port.in;

import org.apache.commons.lang3.tuple.Pair;
import pl.krasmap.submission.application.domain.Krasnal;
import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.domain.submission.SubmissionStatus;

import java.util.List;

public interface SubmissionControllerInterface {
    Submission PostSubmission(NewSubmission submission);
    SubmissionStatus CheckSubmission(int subId);
    List<Submission> GetSubmissionsFromUser(int userId);
    Pair<Submission, Krasnal> GetSubmission(int subId);
    Boolean RejectSubmission(int userId, int subId, String reason);
    Krasnal AcceptSubmission(int userId, int subId);
    Submission UpdateSubmission(int subId, NewSubmission submission);
    List<Submission> GetAllSubmissions();
}
