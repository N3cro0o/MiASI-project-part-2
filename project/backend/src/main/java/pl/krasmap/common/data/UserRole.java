package pl.krasmap.common.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    Guest,
    Wanderer,
    Editor,
    Admin;

    public boolean RoleCheck(UserRole target) {
        if (this == Guest) return false;
        if (this == Wanderer && target != Wanderer) return false;
        if (this == Editor && target == Admin) return false;
        return true;
    }

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
