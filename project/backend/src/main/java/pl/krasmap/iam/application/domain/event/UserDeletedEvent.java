package pl.krasmap.iam.application.domain.event;

import pl.krasmap.common.data.UserRole;

public record UserDeletedEvent(int userId, UserRole role, String login) {
}
