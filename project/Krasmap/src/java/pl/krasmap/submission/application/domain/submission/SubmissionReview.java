package pl.krasmap.submission.application.domain.submission;

import java.time.LocalDateTime;

public record SubmissionReview(int reviewerId, String rejectionReason, LocalDateTime reviewTime) {
}
