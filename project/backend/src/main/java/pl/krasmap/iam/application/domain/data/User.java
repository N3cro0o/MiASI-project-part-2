package pl.krasmap.iam.application.domain.data;

import pl.krasmap.common.data.UserRole;

import java.time.OffsetDateTime;

public record User(int id, String login, String email, UserRole role, boolean active, OffsetDateTime created) {
    public static User newObject(int id, String login, String email, UserRole role, boolean active, OffsetDateTime created) {
        return new User(id, login, email, role, active, created);
    }

    public static User newObject(int id, String login, String email, UserRole role, boolean active) {
        return User.newObject(id, login, email, role, active, OffsetDateTime.now());
    }

    public static User dummy() {
        return User.newObject(-1, "debil", "debil@pwr.edu.pl", UserRole.Guest, false);
    }

    public static User from(UserWeb user) {
        return User.newObject(0, user.login(), user.email(), user.role(), user.active());
    }

    public static User from(int userId, UserWeb user) {
        return User.newObject(userId, user.login(), user.email(), user.role(), user.active());
    }

    public boolean isNull() {
        return login().isEmpty() && email().isEmpty() && id <= 0;
    }
}
