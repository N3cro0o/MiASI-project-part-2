package pl.krasmap.interaction.application.domain.event.review;

public record ReviewUpdatedEvent(int id, int krasnalId, int userId) {
}
