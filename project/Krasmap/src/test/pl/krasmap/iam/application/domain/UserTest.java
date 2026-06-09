package pl.krasmap.iam.application.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;

public class UserTest {

    @Test
    void testNewObject_fieldsSetCorrectly() {
        OffsetDateTime created = OffsetDateTime.parse("2026-01-01T12:00:00+00:00");
        User u = User.newObject(4, "login", "mail@test.com", UserRole.Editor, true, created);

        assertEquals(4, u.id());
        assertEquals("login", u.login());
        assertEquals("mail@test.com", u.email());
        assertEquals(UserRole.Editor, u.role());
        assertTrue(u.active());
        assertEquals(created, u.created());
    }

    @Test
    void testNewObject_withoutCreated_setsCreatedCloseToNow() {
        OffsetDateTime before = OffsetDateTime.now();
        User u = User.newObject(4, "login", "mail@test.com", UserRole.Editor, true);
        OffsetDateTime after = OffsetDateTime.now();

        assertNotNull(u.created());

        OffsetDateTime created = u.created();
        assertFalse(created.isAfter(after), "created shouldn't be later than after");
        assertFalse(created.isBefore(before), "created shouldn't be sooner than before");
    }

    @Test
    void testDummy_values() {
        User dummy = User.dummy();

        assertEquals(-1, dummy.id());
        assertEquals("debil", dummy.login());
        assertEquals("debil@pwr.edu.pl", dummy.email());
        assertEquals(UserRole.Guest, dummy.role());
        assertFalse(dummy.active());
        assertNotNull(dummy.created(), "dummy.created should be set");
    }
}
