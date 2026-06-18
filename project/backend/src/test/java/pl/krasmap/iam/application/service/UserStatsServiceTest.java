package pl.krasmap.iam.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krasmap.iam.application.domain.data.stats.UserReview;
import pl.krasmap.iam.application.domain.data.stats.UserStats;
import pl.krasmap.iam.application.domain.data.stats.UserSubmission;
import pl.krasmap.iam.application.domain.data.stats.UserVisits;
import pl.krasmap.iam.application.port.out.GetUserReviewsInterface;
import pl.krasmap.iam.application.port.out.GetUserSubmissionsInterface;
import pl.krasmap.iam.application.port.out.GetUserVisitsInterface;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserStatsServiceTest {

    @Mock
    private GetUserReviewsInterface userReviews;

    @Mock
    private GetUserSubmissionsInterface userSubs;

    @Mock
    private GetUserVisitsInterface userVisits;

    @InjectMocks
    private UserStatsService userStatsService;

    private static final int USER_ID = 41;

    @Test
    void testGetUserStats_correctlyAggregatesSubmissions() {
        List<UserSubmission> submissions = List.of(
            mock(UserSubmission.class),
            mock(UserSubmission.class),
            mock(UserSubmission.class)
        );

        when(userSubs.GetUserSubmissions(USER_ID)).thenReturn(submissions);
        when(userReviews.GetUserReviews(USER_ID)).thenReturn(Collections.emptyList());
        when(userVisits.GetUserVisits(USER_ID)).thenReturn(Collections.emptyList());

        UserStats stats = userStatsService.GetUserStats(USER_ID);

        assertNotNull(stats);
        assertEquals(3, stats.subCount());
        assertEquals(0, stats.reviewCount());
        assertEquals(0, stats.krasnalCount());
        verify(userSubs, times(1)).GetUserSubmissions(USER_ID);
    }

    @Test
    void testGetUserStats_correctlyAggregatesReviews() {
        List<UserReview> reviews = List.of(
                mock(UserReview.class),
                mock(UserReview.class)
        );

        when(userSubs.GetUserSubmissions(USER_ID)).thenReturn(Collections.emptyList());
        when(userReviews.GetUserReviews(USER_ID)).thenReturn(reviews);
        when(userVisits.GetUserVisits(USER_ID)).thenReturn(Collections.emptyList());

        UserStats stats = userStatsService.GetUserStats(USER_ID);

        assertNotNull(stats);
        assertEquals(0, stats.subCount());
        assertEquals(2, stats.reviewCount());
        assertEquals(0, stats.krasnalCount());
        verify(userReviews, times(1)).GetUserReviews(USER_ID);
    }

    @Test
    void testGetUserStats_correctlyAggregatesVisits() {
        List<UserVisits> visits = List.of(
                mock(UserVisits.class),
                mock(UserVisits.class)
        );

        when(userSubs.GetUserSubmissions(USER_ID)).thenReturn(Collections.emptyList());
        when(userReviews.GetUserReviews(USER_ID)).thenReturn(Collections.emptyList());
        when(userVisits.GetUserVisits(USER_ID)).thenReturn(visits);

        UserStats stats = userStatsService.GetUserStats(USER_ID);

        assertNotNull(stats);
        assertEquals(0, stats.subCount());
        assertEquals(0, stats.reviewCount());
        assertEquals(2, stats.krasnalCount());
        verify(userVisits, times(1)).GetUserVisits(USER_ID);
    }
}
