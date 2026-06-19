package pl.krasmap.iam.application.domain.event;

import pl.krasmap.common.data.UserRole;

// Fired when a previously deactivated user account is restored by an Admin
public record UserRestoredEvent(int userId, UserRole role, String login) {
}
