package pl.krasmap.submission.application.domain.event;

import pl.krasmap.common.data.SubmissionStatus;

public record SubCreatedEvent(int id, int userId, SubmissionStatus status) {
}
