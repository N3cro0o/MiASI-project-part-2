package pl.krasmap.iam.application.service;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.iam.application.domain.data.UserNew;
import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.domain.service.LoginService;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private HandleUserService userHandle;

    @Mock
    private UserAuthInterface auth;

    @InjectMocks
    private LoginService loginService;

    private static final String LOGIN = "test_user";
    private static final String PASSWORD = "test_password";
    private static final String EMAIL = "test@pwr.edu.pl";
    private static final String JWT = "qwertyuiop1234567890ASDFGHJKL...";

    @Test
    void testCheckLogin_withCorrectCredentials_returnsTrue() {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);
        String hashedPassword = bcrypt.encode(PASSWORD);

        User user = User.newObject(1, LOGIN, EMAIL, UserRole.Wanderer, true, OffsetDateTime.now());

        when(userHandle.GetUserPass(LOGIN)).thenReturn(hashedPassword);
        when(userHandle.GetUser(LOGIN)).thenReturn(user);
        when(auth.GenerateJwt(1)).thenReturn(JWT);

        Pair<Boolean, String> result = loginService.CheckLogin(LOGIN, PASSWORD);

        assertTrue(result.getLeft());
        assertEquals(JWT, result.getRight());
        verify(userHandle, times(1)).GetUserPass(LOGIN);
        verify(userHandle, times(1)).GetUser(LOGIN);
        verify(auth, times(1)).GenerateJwt(1);
    }

    @Test
    void testCheckLogin_withIncorrectPassword_returnsFalse() {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);
        String hashedPassword = bcrypt.encode(PASSWORD);
        String wrongPassword = "wrong_password";

        when(userHandle.GetUserPass(LOGIN)).thenReturn(hashedPassword);

        Pair<Boolean, String> result = loginService.CheckLogin(LOGIN, wrongPassword);

        assertFalse(result.getLeft());
        assertEquals("", result.getRight());
        verify(userHandle, times(1)).GetUserPass(LOGIN);
        verify(auth, never()).GenerateJwt(anyInt());
    }

    @Test
    void testCheckLogin_withNonexistentUser_returnsFalse() {
        when(userHandle.GetUserPass(LOGIN)).thenReturn("");

        Pair<Boolean, String> result = loginService.CheckLogin(LOGIN, PASSWORD);

        assertFalse(result.getLeft());
        assertEquals("", result.getRight());
        verify(userHandle, times(1)).GetUserPass(LOGIN);
        verify(auth, never()).GenerateJwt(anyInt());
    }

    @Test
    void testRegister_withValidUser_returnsTrue() {
        UserNew newUser = new UserNew(LOGIN, EMAIL, PASSWORD);
        User createdUser = User.newObject(1, LOGIN, EMAIL, UserRole.Wanderer, true, OffsetDateTime.now());

        when(userHandle.AddUser(any(UserWeb.class))).thenReturn(createdUser);
        when(auth.GenerateJwt(1)).thenReturn(JWT);

        Pair<Boolean, String> result = loginService.Register(newUser);

        assertTrue(result.getLeft());
        assertEquals(JWT, result.getRight());
        verify(userHandle, times(1)).AddUser(any(UserWeb.class));
        verify(auth, times(1)).GenerateJwt(1);
    }

    @Test
    void testRegister_whenAddUserFails_returnsFalse() {
        UserNew newUser = new UserNew(LOGIN, EMAIL, PASSWORD);

        when(userHandle.AddUser(any(UserWeb.class))).thenReturn(null);

        Pair<Boolean, String> result = loginService.Register(newUser);

        assertFalse(result.getLeft());
        assertEquals("", result.getRight());
        verify(userHandle, times(1)).AddUser(any(UserWeb.class));
        verify(auth, never()).GenerateJwt(anyInt());
    }

    @Test
    void testRegister_passwordIsHashed() {
        UserNew newUser = new UserNew(LOGIN, EMAIL, PASSWORD);
        User createdUser = User.newObject(1, LOGIN, EMAIL, UserRole.Wanderer, true, OffsetDateTime.now());

        when(userHandle.AddUser(any(UserWeb.class))).thenReturn(createdUser);
        when(auth.GenerateJwt(1)).thenReturn(JWT);

        loginService.Register(newUser);

        verify(userHandle, times(1)).AddUser(argThat(userWeb ->
                !userWeb.password().equals(PASSWORD)
        ));
    }
}
