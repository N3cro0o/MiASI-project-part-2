package pl.krasmap.submission.application.port.in;

import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.submission.SumbissionStatus;

public interface SubmissionControllerInterface {
    void PostSubmission(NewSubmission submission);
    SumbissionStatus CheckSubmission(int subId);
}
