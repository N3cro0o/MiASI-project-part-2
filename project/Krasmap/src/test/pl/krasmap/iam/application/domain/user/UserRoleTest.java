package pl.krasmap.iam.application.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserRoleTest {

    @Test
    void fromString_knownRoles_shouldReturnProperEnum() {
        assertEquals(UserRole.Wanderer, UserRole.FromString("wanderer"));
        assertEquals(UserRole.Wanderer, UserRole.FromString("Wanderer"));
        assertEquals(UserRole.Admin, UserRole.FromString("admin"));
        assertEquals(UserRole.Editor, UserRole.FromString("EDITOR"));
    }

    @Test
    void fromString_unknownRole_shouldReturnGuest() {
        assertEquals(UserRole.Guest, UserRole.FromString("cokolwiek"));
        assertEquals(UserRole.Guest, UserRole.FromString(""));
    }

    @Test
    void toString_shouldReturnExpectedFormat() {
        assertEquals("WANDERER", UserRole.Wanderer.toString());
        assertEquals("GUEST", UserRole.Guest.toString());
        assertEquals("EDITOR", UserRole.Editor.toString());
        assertEquals("ADMIN", UserRole.Admin.toString());
    }
}
