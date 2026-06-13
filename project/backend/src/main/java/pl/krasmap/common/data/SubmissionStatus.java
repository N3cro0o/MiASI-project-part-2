package pl.krasmap.common.data;

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


    @Override
    public String toString() {
        return switch (this) {
            case Pending -> "PENDING";
            case Accepted -> "ACCEPTED";
            case Rejected -> "REJECTED";
        };
    }
}
