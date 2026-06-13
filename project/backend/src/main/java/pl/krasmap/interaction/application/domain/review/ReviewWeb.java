package pl.krasmap.interaction.application.domain.review;

public record ReviewWeb(int krasnalId, int userId, short rating, String content) {
}
