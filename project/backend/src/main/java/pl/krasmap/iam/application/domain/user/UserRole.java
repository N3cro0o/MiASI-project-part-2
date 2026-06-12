package pl.krasmap.iam.application.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    Guest,
    Wanderer,
    Editor,
    Admin;

    @JsonCreator
    public static UserRole FromString(String role) {
        return switch (role.toLowerCase()) {
            case "wanderer" -> UserRole.Wanderer;
            case "admin" -> UserRole.Admin;
            case "editor" -> UserRole.Editor;
            default -> UserRole.Guest;
        };
    }


    @Override
    @JsonValue
    public String toString() {
        return switch (this) {
            case Guest -> "GUEST";
            case Wanderer -> "WANDERER";
            case Editor -> "EDITOR";
            case Admin -> "ADMIN";
        };
    }
}
