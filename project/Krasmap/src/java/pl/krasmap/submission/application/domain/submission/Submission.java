package pl.krasmap.submission.application.domain.submission;

public record Submission(int id, int userId, String json, SumbissionStatus status, SubmissionReview review) {
}
