package pl.krasmap.iam.application.domain;

import java.time.LocalDateTime;

public record User(int id, String email, UserRole role, boolean active, LocalDateTime created) {
    public static User newObject(int id, String email, UserRole role, boolean active, LocalDateTime created) {
        return new User(id, email, role, active, created);
    }

    public static User newObject(int id, String email, UserRole role, boolean active) {
        return User.newObject(id, email, role, active, LocalDateTime.now());
    }

    public static User dummy() {
        return User.newObject(-1, "debil@pwr.edu.pl", UserRole.Guest, false);
    }

}
