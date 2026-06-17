package pl.krasmap.submission.application.domain.data;

import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.SubmissionStatus;

import java.time.OffsetDateTime;

public record SubmissionReturn(int id, int userId, SubmissionStatus status, OffsetDateTime time, String krasnalName, Position krasnalPos) {
}
