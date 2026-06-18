package pl.krasmap.iam.application.domain;

import org.junit.jupiter.api.Test;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.domain.data.UserWeb;


import static org.junit.jupiter.api.Assertions.*;

public class UserWebTest {

    @Test
    void testUserWebFrom_shouldCopyFieldsAndReplacePassword() {
        UserWeb oldUser = new UserWeb(
                "debil123",
                "debil@pwr.edu.pl",
                "pass123",
                UserRole.Wanderer,
                true
        );

        String newPassword = "Pass#123#";

        UserWeb newUser = UserWeb.from(oldUser, newPassword);

        assertEquals(newPassword, newUser.password(), "Password should be the same as new password");
        assertEquals(oldUser.login(), newUser.login(), "Login should be the same as old login");
        assertEquals(oldUser.email(), newUser.email(), "Email should be the same as old email");
        assertEquals(oldUser.role(), newUser.role(), "Role should be the same as old role");
        assertEquals(oldUser.active(), newUser.active(), "Activity status should be the same as old activity status");
    }

    @Test
    void testUserWebFrom_fieldsSetCorrectly() {
        UserWeb user = new UserWeb(
                "debil123",
                "debil@pwr.edu.pl",
                "pass123",
                UserRole.Admin,
                true
        );

        assertEquals("debil123", user.login());
        assertEquals("debil@pwr.edu.pl", user.email());
        assertEquals("pass123", user.password());
        assertEquals(UserRole.Admin, user.role());
        assertTrue(user.active());
    }
}
