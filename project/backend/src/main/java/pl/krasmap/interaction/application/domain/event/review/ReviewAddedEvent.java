package pl.krasmap.interaction.application.domain.event.review;

public record ReviewAddedEvent(int id, int krasnalId, int userId) {
}
