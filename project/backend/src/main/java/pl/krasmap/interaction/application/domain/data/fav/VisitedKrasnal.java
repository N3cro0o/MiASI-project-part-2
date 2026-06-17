package pl.krasmap.interaction.application.domain.data.fav;

import java.time.OffsetDateTime;

public record VisitedKrasnal(int id, int krasnalId, int userId, OffsetDateTime time) {
}
