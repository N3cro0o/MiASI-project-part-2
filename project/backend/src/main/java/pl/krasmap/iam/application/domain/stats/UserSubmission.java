package pl.krasmap.iam.application.domain.stats;

import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.domain.ReviewKrasnal;
import pl.krasmap.submission.application.domain.submission.Submission;

import java.time.OffsetDateTime;

public record UserSubmission(int id, String name, Position pos, KrasnalCategory cat, SubmissionStatus status, OffsetDateTime submittedTime, String description, String comment) {

    public static UserSubmission From(Submission s, ReviewKrasnal k) {
        String comment = s.review() != null ? s.review().rejectionReason() : null;
        return new UserSubmission(s.id(), k.name(), k.position(), k.category(), s.status(), s.submittedTime(), k.description(), comment);
    }
}
