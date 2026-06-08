package pl.krasmap.iam.application.domain;

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

}
