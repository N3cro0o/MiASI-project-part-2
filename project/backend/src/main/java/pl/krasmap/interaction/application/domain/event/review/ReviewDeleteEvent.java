package pl.krasmap.interaction.application.domain.event.review;

public record ReviewDeleteEvent(int id, int krasnalId, int userId) {
}
