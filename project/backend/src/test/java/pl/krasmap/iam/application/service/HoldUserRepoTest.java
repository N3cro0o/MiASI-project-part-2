package pl.krasmap.iam.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.port.out.UserFetchInterface;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HoldUserRepoTest {

    @Mock
    private UserFetchInterface fetch;

    @InjectMocks
    private HoldUserRepo repo;

    @Test
    void testGetUser_callsInterfaceAndReturnsUser() {
        int userId = 1;
        OffsetDateTime now = OffsetDateTime.now();
        User expectedUser = User.newObject(userId, "lorem", "ipsum@pwr.edu.pl", UserRole.Wanderer, true, now);

        when(fetch.GetUser(userId)).thenReturn(expectedUser);

        User result = repo.GetUser(userId);

        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(fetch, times(1)).GetUser(userId);
    }

    @Test
    void testGetUserList_returnsAllUsers() {
        User u1 = User.newObject(1, "user1", "u1@test.com", UserRole.Guest, false, OffsetDateTime.now());
        User u2 = User.newObject(2, "user2", "u2@test.com", UserRole.Wanderer, true, OffsetDateTime.now());

        when(fetch.GetAllUsers()).thenReturn(List.of(u1, u2));

        var result = repo.GetUserList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(u1));
        assertTrue(result.contains(u2));
        verify(fetch, times(1)).GetAllUsers();
    }

    @Test
    void testAddUser_callsSaveAndGet() {
        UserWeb user = new UserWeb("debil123", "debil@pwr.edu.pl", "pass123", UserRole.Editor, true);
        int generatedId = 1;

        when(fetch.SaveUser(user)).thenReturn(generatedId);

        int result = repo.AddUser(user);

        assertEquals(generatedId, result);
        verify(fetch, times(1)).SaveUser(user);
    }

    @Test
    void testUpdateUser_callsUpdateAndGet() {
        int userId = 2;
        UserWeb user = new UserWeb("debil123", "debil@pwr.edu.pl", "pass123", UserRole.Editor, true);

        when(fetch.UpdateUser(userId, user)).thenReturn(userId);

        int result = repo.UpdateUser(userId, user);


        assertEquals(userId, result);
        verify(fetch, times(1)).UpdateUser(userId, user);
    }

    @Test
    void testDeleteUser_callsHideUserAndReturnsTrue() {
        int userId = 3;
        when(fetch.HideUser(userId)).thenReturn(true);

        boolean result = repo.DeleteUser(userId);

        assertTrue(result);
        verify(fetch, times(1)).HideUser(userId);
    }

    @Test
    void testDeleteUser_callsHideUserAndReturnsFalse() {
        int userId = 4;
        when(fetch.HideUser(userId)).thenReturn(false);

        boolean result = repo.DeleteUser(userId);

        assertFalse(result);
        verify(fetch, times(1)).HideUser(userId);
    }

    @Test
    void testGetUser_whenFetchReturnsNull_shouldReturnNull() {
        int userId = -1;
        when(fetch.GetUser(userId)).thenReturn(null);

        User result = repo.GetUser(userId);

        assertNull(result);
        verify(fetch, times(1)).GetUser(userId);
    }
}
