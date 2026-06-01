package pl.krasmap.interaction.application.domain;

import pl.krasmap.iam.application.domain.User;

import java.time.LocalDateTime;

public record Review(int id, int krasnalId, int userId, short rating, String content, LocalDateTime created) {

}
