package pl.krasmap.iam.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.debug.DatabaseCheck;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.iam.application.domain.data.UserWeb;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class HandleUserServiceIntegrationTest {

    @Autowired
    private HandleUserService handleUserService;

    @Autowired
    private ApplicationContext context;

    @Test
    void printPassword() {
        Environment env = context.getEnvironment();
        System.out.println("spring.datasource.password = " + env.getProperty("spring.datasource.password"));
        System.out.println("db.password = " + env.getProperty("db.password"));
    }

    @Test
    void testAddUserAndRetrieve_integrationFlow() {
        UserWeb newUser = new UserWeb(
                "testuser",
                "test@pwr.edu.pl",
                "password_123",
                UserRole.FromString("wanderer"),
                true
        );

        User addedUser = handleUserService.AddUser(newUser);
        User retrievedUser = handleUserService.GetUser(addedUser.id());

        assertNotNull(addedUser);
        assertNotNull(retrievedUser);
        assertEquals(newUser.login(), retrievedUser.login());
        assertEquals(newUser.email(), retrievedUser.email());
        assertEquals(UserRole.Wanderer, retrievedUser.role());
    }

    @Test
    void testUpdateUser_persistsChangesToDatabase() {
        UserWeb newUser = new UserWeb(
                "original_user",
                "original@pwr.edu.pl",
                "original_pass",
                UserRole.Guest,
                true
        );
        User addedUser = handleUserService.AddUser(newUser);

        UserWeb updatedUserData = new UserWeb(
                "updated_user",
                "updated@pwr.edu.pl",
                "new_pass_123",
                UserRole.Editor,
                false
        );

        User updated = handleUserService.UpdateUser(addedUser.id(), updatedUserData);
        User retrieved = handleUserService.GetUser(addedUser.id());

        assertEquals("updated_user", retrieved.login());
        assertEquals("updated@pwr.edu.pl", retrieved.email());
        assertEquals(UserRole.Editor, retrieved.role());
    }

    @Test
    void testDeleteUser_marksUserAsInactive() {
        UserWeb newUser = new UserWeb(
                "deleted_user",
                "deleted@pwr.edu.pl",
                "password123",
                UserRole.Wanderer,
                true
        );
        User addedUser = handleUserService.AddUser(newUser);

        boolean deleted = handleUserService.DeleteUser(addedUser.id());
        User retrievedAfterDelete = handleUserService.GetUser(addedUser.id());

        assertTrue(deleted);
        assertNull(retrievedAfterDelete);
    }

    @Test
    void testGetUserList_returnsAllActiveUsers() {
        handleUserService.AddUser(new UserWeb(
                "user1",
                "u1@pwr.edu.pl",
                "pass123",
                UserRole.Wanderer,
                true
        ));
        handleUserService.AddUser(new UserWeb(
                "user2",
                "u2@pwr.edu.pl",
                "pass123",
                UserRole.Wanderer,
                true
        ));

        List<User> users = handleUserService.GetUserList();

        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }
}
