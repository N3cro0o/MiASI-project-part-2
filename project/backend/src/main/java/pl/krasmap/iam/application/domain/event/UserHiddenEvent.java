package pl.krasmap.iam.application.domain.event;

import pl.krasmap.common.data.UserRole;

public record UserHiddenEvent(int userId, UserRole role, String login) {
}
