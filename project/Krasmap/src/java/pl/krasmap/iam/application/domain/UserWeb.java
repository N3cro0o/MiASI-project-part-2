package pl.krasmap.iam.application.domain;

import pl.krasmap.iam.application.domain.user.UserRole;

public record UserWeb(String login, String email, String password, UserRole role, boolean active) {

    public static UserWeb from(UserWeb oldUser, String password) {
        return new UserWeb(oldUser.login(), oldUser.email(), password, oldUser.role(), oldUser.active());
    }
}
