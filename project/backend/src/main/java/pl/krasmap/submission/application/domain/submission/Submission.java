package pl.krasmap.submission.application.domain.submission;

import pl.krasmap.common.data.SubmissionStatus;

import java.time.OffsetDateTime;

public record Submission(int id, int userId, String json, SubmissionStatus status, OffsetDateTime submittedTime, SubmissionReview review) {

    public static Submission newObject(int id, int userId, String json, SubmissionStatus stat, OffsetDateTime submittedTime) {
        return new Submission(id, userId, json, stat, submittedTime, null);
    }

    public static Submission newObject(int id, int userId, String json, SubmissionStatus stat, OffsetDateTime submittedTime, SubmissionReview review) {
        return new Submission(id, userId, json, stat,submittedTime, review);
    }

    public Submission With(SubmissionStatus status, SubmissionReview rev) {
        return new Submission(this.id, this.userId, this.json, status, this.submittedTime, rev);
    }
}
