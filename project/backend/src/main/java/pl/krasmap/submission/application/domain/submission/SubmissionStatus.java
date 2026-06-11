package pl.krasmap.submission.application.domain.submission;

public enum SubmissionStatus {
    Pending,
    Accepted,
    Rejected;

    public static SubmissionStatus FromString(String str) {
        return switch (str.toLowerCase()) {
            case "accepted" -> SubmissionStatus.Accepted;
            case "rejected" -> SubmissionStatus.Rejected;
            default -> SubmissionStatus.Pending;
        };
    }
}
