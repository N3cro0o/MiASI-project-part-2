package pl.krasmap.submission.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.data.NewSubmission;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.port.out.SubmissionFetchInterface;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HoldSubmissionRepoTest {

    @Mock
    private SubmissionFetchInterface fetch;

    @InjectMocks
    private HoldSubmissionRepo repo;

    @Test
    void testGetSubmission_callsInterfaceAndReturnsSubmission() {
        int subId = 3;
        Submission submission = Submission.newObject(subId, 30, "{test: \"test\"}", SubmissionStatus.Pending, OffsetDateTime.now());

        when(fetch.GetSubmission(subId)).thenReturn(submission);

        Submission result = repo.GetSubmission(subId);

        assertNotNull(result);
        assertEquals(submission, result);
        verify(fetch, times(1)).GetSubmission(subId);
        verifyNoMoreInteractions(fetch);
    }

    @Test
    void testGetSubmissionsFromUser_callsInterfaceAndReturnsSubmissionList() {
        int userId = 4;
        Submission s1 = mock(Submission.class);
        Submission s2 = mock(Submission.class);

        List<Submission> submissions = List.of(s1, s2);
        when(fetch.GetSubmissionsFromUser(userId)).thenReturn(submissions);

        List<Submission> result = repo.GetSubmissionsFromUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(s1, result.get(0));
        verify(fetch, times(1)).GetSubmissionsFromUser(userId);
        verifyNoMoreInteractions(fetch);
    }

    @Test
    void testAddSubmission_callsAddAndReturnsId() {
        int subId = 32;
        NewSubmission newSub = new NewSubmission(4, mock(ReviewKrasnal.class));

        when(fetch.AddSubmission(newSub)).thenReturn(subId);

        int result = repo.AddSubmission(newSub);

        assertSame(subId, result);
        verify(fetch, times(1)).AddSubmission(newSub);
        verifyNoMoreInteractions(fetch);
    }

    @Test
    void testCheckSubmission_callsInterfaceAndReturnsSubStatus() {
        int subId = 40;
        SubmissionStatus status = SubmissionStatus.Pending;

        when(fetch.CheckSubmission(subId)).thenReturn(status);

        SubmissionStatus result = repo.CheckSubmission(subId);

        assertNotNull(result);
        assertEquals(status, result);
        verify(fetch, times(1)).CheckSubmission(subId);
        verifyNoMoreInteractions(fetch);
    }

    @Test
    void testUpdateSubReview_callsInterfaceAndReturnsFalse() {
        Submission newSub = mock(Submission.class);

        when(fetch.UpdateSubReview(newSub)).thenReturn(false);

        boolean result = repo.UpdateSubReview(newSub);

        assertFalse(result);
        verify(fetch, times(1)).UpdateSubReview(newSub);
        verifyNoMoreInteractions(fetch);
    }

    @Test
    void testUpdateSubmission_callsUpdateAndReturnsId() {
        int subId = 75;
        NewSubmission newSub = new NewSubmission(4, mock(ReviewKrasnal.class));

        when(fetch.UpdateSubmission(subId, newSub)).thenReturn(subId);

        int result = repo.UpdateSubmission(subId, newSub);

        assertSame(subId, result);
        verify(fetch, times(1)).UpdateSubmission(subId, newSub);
        verifyNoMoreInteractions(fetch);
    }
}
