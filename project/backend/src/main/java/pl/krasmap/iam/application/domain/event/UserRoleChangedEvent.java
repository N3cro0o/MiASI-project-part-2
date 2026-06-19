package pl.krasmap.iam.application.domain.event;

import pl.krasmap.common.data.UserRole;

// Fired when an Admin changes a user's role
public record UserRoleChangedEvent(int userId, UserRole oldRole, UserRole newRole, String login) {
}
