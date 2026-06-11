package pl.krasmap.interaction.application.domain;

public record ReviewWeb(int krasnalId, int userId, short rating, String content) {
}
