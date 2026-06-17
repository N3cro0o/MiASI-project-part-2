package pl.krasmap.submission.application.domain.event;


public record SubmissionRejectedEvent(int id, int userId, String rejectionReason) {
}
