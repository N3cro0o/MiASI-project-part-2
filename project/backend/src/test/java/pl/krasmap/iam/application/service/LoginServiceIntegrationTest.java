package pl.krasmap.iam.application.service;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.iam.application.domain.data.UserNew;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LoginServiceIntegrationTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private HandleUserService handleUserService;

//    private static final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);

    @Test
    void testRegisterAndLogin_completeFlow() {
        String login = "newuser";
        String password = "testpass123";
        UserNew newUser = new UserNew(login, "newuser@pwr.edu.pl", password);

        Pair<Boolean, String> registerResult = loginService.Register(newUser);

        assertTrue(registerResult.getLeft());
        assertNotNull(registerResult.getRight());
        assertNotEquals("", registerResult.getRight());

        Pair<Boolean, String> loginResult = loginService.CheckLogin(login, password);

        assertTrue(loginResult.getLeft());
        assertNotNull(loginResult.getRight());
        assertNotEquals("", loginResult.getRight());
    }

    @Test
    void testRegister_createsUserInDatabase() {
        UserNew newUser = new UserNew("dbtest", "dbtest@pwr.edu.pl", "testpass123");

        Pair<Boolean, String> result = loginService.Register(newUser);
        User retrievedUser = handleUserService.GetUser("dbtest");

        assertTrue(result.getLeft());
        assertNotNull(retrievedUser);
        assertEquals("dbtest@pwr.edu.pl", retrievedUser.email());
    }

    @Test
    void testCheckLogin_afterRegistration_succeeds() {
        UserNew newUser = new UserNew("logintest", "logintest@test.com", "securepass");
        loginService.Register(newUser);

        Pair<Boolean, String> result = loginService.CheckLogin("logintest", "securepass");

        assertTrue(result.getLeft());
        assertNotNull(result.getRight());
    }
}
