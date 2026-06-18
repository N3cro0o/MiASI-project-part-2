package pl.krasmap.iam.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.domain.event.UserCreatedEvent;
import pl.krasmap.iam.application.domain.event.UserDeletedEvent;
import pl.krasmap.iam.application.domain.event.UserUpdatedEvent;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HandleUserServiceTest {

    @Mock
    private HoldUserRepo repo;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private HandleUserService handleUserService;

    @Test
    void testGetUser_withId_callsRepoAndReturnsUser() {
        int userId = 75;
        User user = User.newObject(userId, "login", "test@pwr.edu.pl", UserRole.Wanderer, true, OffsetDateTime.now());

        when(repo.GetUser(userId)).thenReturn(user);

        User result = handleUserService.GetUser(userId);

        assertNotNull(result);
        assertEquals(user, result);
        verify(repo, times(1)).GetUser(userId);
    }

    @Test
    void testGetUser_withLogin_callsRepoAndReturnsUser() {
        String userLogin = "login";
        User user = User.newObject(3, userLogin, "test@pwr.edu.pl", UserRole.Admin, true, OffsetDateTime.now());

        when(repo.GetUser(userLogin)).thenReturn(user);

        User result = handleUserService.GetUser(userLogin);

        assertNotNull(result);
        assertEquals(user, result);
        verify(repo, times(1)).GetUser(userLogin);
    }

    @Test
    void testGetUserPass_callsRepoAndReturnsPassword() {
        String hashedPassword = "hashedPassword";
        String userLogin = "login";
        User user = User.newObject(3, userLogin, "test@pwr.edu.pl", UserRole.Admin, true, OffsetDateTime.now());

        when(repo.GetUserPass(userLogin)).thenReturn(hashedPassword);

        String result = handleUserService.GetUserPass(userLogin);

        assertEquals(hashedPassword, result);
        verify(repo, times(1)).GetUserPass(userLogin);
    }

    @Test
    void testGetUserList_callsRepoAndReturnsUserList() {
        int firstUserId = 75;
        List<User> users = List.of(
                User.newObject(firstUserId, "login", "test@pwr.edu.pl", UserRole.Admin, true, OffsetDateTime.now()),
                mock(User.class),
                mock(User.class)
        );

        when(repo.GetUserList()).thenReturn(users);

        List<User> result = handleUserService.GetUserList();

        assertNotNull(result);
        assertEquals(users, result);
        assertEquals(3, result.size());
        assertEquals(firstUserId, result.get(0).id());
    }

    @Test
    void testAddUser_AddsUserToRepoAndPublishesEvent() {
        int userId = 34;
        UserWeb toAdd = new UserWeb("login", "test@pwr.edu.pl", "testpass", UserRole.Wanderer, true);
        User user = User.from(userId, toAdd);

        when(repo.AddUser(toAdd)).thenReturn(userId);
        when(handleUserService.GetUser(userId)).thenReturn(user);

        User result = handleUserService.AddUser(toAdd);

        assertNotNull(result);
        assertEquals(user, result);
        verify(repo, times(1)).AddUser(toAdd);
        verify(events, times(1)).publishEvent(any(UserCreatedEvent.class));
    }

    @Test
    void testUpdateUser_UpdatesUserInRepoAndPublishesEvent() {
        int userId = 34;
        UserWeb toAdd = new UserWeb("login", "test@pwr.edu.pl", "testpass", UserRole.Wanderer, true);
        User user = User.from(userId, toAdd);

        when(repo.UpdateUser(userId, toAdd)).thenReturn(userId);
        when(handleUserService.GetUser(userId)).thenReturn(user);

        User result = handleUserService.UpdateUser(userId, toAdd);

        assertNotNull(result);
        assertEquals(user, result);
        verify(repo, times(1)).UpdateUser(userId, toAdd);
        verify(events, times(1)).publishEvent(any(UserUpdatedEvent.class));
    }

    @Test
    void testDeleteUser_DeleteUserFromRepoAndPublishesEvent() {
        int userId = 34;
        User user = User.newObject(userId, "login", "test@pwr.edu.pl", UserRole.Admin, true, OffsetDateTime.now());

        when(repo.DeleteUser(userId)).thenReturn(true);
        when(handleUserService.GetUser(userId)).thenReturn(user);

        boolean result = handleUserService.DeleteUser(userId);

        assertTrue(result);
        verify(repo, times(1)).DeleteUser(userId);
        verify(events, times(1)).publishEvent(any(UserDeletedEvent.class));
    }

    @Test
    void testDeleteUser_RepoReturnsFalse_EventNotPublished() {
        int userId = 34;
        User user = User.newObject(userId, "login", "test@pwr.edu.pl", UserRole.Admin, true, OffsetDateTime.now());

        when(repo.DeleteUser(userId)).thenReturn(false);
        when(handleUserService.GetUser(userId)).thenReturn(user);

        boolean result = handleUserService.DeleteUser(userId);

        assertFalse(result);
        verify(repo, times(1)).DeleteUser(userId);
        verify(events, never()).publishEvent(any(UserDeletedEvent.class));
    }
}
