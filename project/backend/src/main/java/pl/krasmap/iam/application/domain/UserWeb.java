package pl.krasmap.iam.application.domain;

import pl.krasmap.common.data.UserRole;

public record UserWeb(String login, String email, String password, UserRole role, boolean active) {

    public static UserWeb from(UserWeb oldUser, String password) {
        return new UserWeb(oldUser.login(), oldUser.email(), password, oldUser.role(), oldUser.active());
    }

    public static UserWeb from(UserNew newUser) {
        return new UserWeb(newUser.login(), newUser.email(), newUser.password(), UserRole.Wanderer, true);
    }

    public static UserWeb from(UserNew newUser, String pass) {
        return new UserWeb(newUser.login(), newUser.email(), pass, UserRole.Wanderer, true);
    }
}
