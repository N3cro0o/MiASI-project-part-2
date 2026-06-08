package pl.krasmap.iam.application.domain;

public enum UserRole {
    Guest,
    Wanderer,
    Editor,
    Admin;

    public static UserRole FromString(String role) {
        return switch (role.toLowerCase()) {
            case "wanderer" -> UserRole.Wanderer;
            case "admin" -> UserRole.Admin;
            case "editor" -> UserRole.Editor;
            default -> UserRole.Guest;
        };
    }
}
