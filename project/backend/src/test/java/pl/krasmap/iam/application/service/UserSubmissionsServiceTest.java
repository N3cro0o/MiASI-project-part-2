package pl.krasmap.iam.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krasmap.iam.application.domain.data.stats.UserSubmission;
import pl.krasmap.iam.application.port.out.GetUserSubmissionsInterface;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSubmissionsServiceTest {

    @Mock
    private GetUserSubmissionsInterface getUserSubs;

    @InjectMocks
    private UserSubmissionsService userSubs;

    @Test
    void testGetUserSubs_userExists_returnsUserSubList() {
        int userId = 50;

        List<UserSubmission> submissions = List.of(
                mock(UserSubmission.class),
                mock(UserSubmission.class),
                mock(UserSubmission.class)
        );

        when(getUserSubs.GetUserSubmissions(userId)).thenReturn(submissions);

        List<UserSubmission> result = userSubs.GetUserSubs(userId);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(getUserSubs, times(1)).GetUserSubmissions(userId);
    }
}
