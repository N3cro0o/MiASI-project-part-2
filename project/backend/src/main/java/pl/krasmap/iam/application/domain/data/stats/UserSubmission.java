package pl.krasmap.iam.application.domain.data.stats;

import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.data.submission.Submission;

import java.time.OffsetDateTime;

public record UserSubmission(int id, String name, Position pos, KrasnalCategory cat, SubmissionStatus status, OffsetDateTime submittedTime) {

    public static UserSubmission From(Submission s, ReviewKrasnal k) {
        return new UserSubmission(s.id(), k.name(), k.position(), k.category(), s.status(), s.submittedTime());
    }
}
