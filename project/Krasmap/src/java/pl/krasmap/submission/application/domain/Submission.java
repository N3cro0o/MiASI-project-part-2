package pl.krasmap.submission.application.domain;

public record Submission(int id, int userId, String json, SumbissionStatus status, SubmissionReview review) {
}
