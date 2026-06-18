package pl.krasmap.submission.application.domain.event;

import pl.krasmap.submission.application.domain.data.ReviewKrasnal;

public record SubmissionAcceptedEvent(int subId, int userId, ReviewKrasnal krasnal) {
}
