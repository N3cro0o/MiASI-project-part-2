package pl.krasmap.interaction.application.domain;

import java.time.LocalDateTime;

public record Review(int id, int krasnalId, int userId, short rating, String content, LocalDateTime created) {

}
