package pl.krasmap.submission.application.domain.data.submission;

import java.time.OffsetDateTime;

public record SubmissionReview(int reviewerId, String rejectionReason, OffsetDateTime reviewTime) {

    public static SubmissionReview newObject(int reviewerId, OffsetDateTime reviewTime) {
        return new SubmissionReview(reviewerId, "Accepted", reviewTime);
    }

    public static SubmissionReview newObject(int reviewerId, String rejectionReason, OffsetDateTime reviewTime) {
        return new SubmissionReview(reviewerId, rejectionReason, reviewTime);
    }

}
