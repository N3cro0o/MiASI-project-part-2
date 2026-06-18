package pl.krasmap.submission.application.domain.event;

import pl.krasmap.common.data.SubmissionStatus;

public record SubUpdatedEvent(int id, int userId, SubmissionStatus status) {
}
