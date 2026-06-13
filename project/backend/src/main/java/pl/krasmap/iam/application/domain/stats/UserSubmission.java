package pl.krasmap.iam.application.domain.stats;

import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.domain.submission.Submission;

import java.time.OffsetDateTime;

public record UserSubmission(int id, String json, SubmissionStatus status, OffsetDateTime submittedTime) {
    public static UserSubmission From(Submission s) {
        return new UserSubmission(s.id(), s.json(), s.status(), s.submittedTime());
    }
}
