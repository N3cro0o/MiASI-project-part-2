package pl.krasmap.submission.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krasmap.submission.application.domain.Krasnal;
import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.domain.submission.SubmissionStatus;
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
    void testAddSubmission_callsAddAndGetSubmission() {
        int subId = 32;
        NewSubmission newSub = new NewSubmission(4, mock(Krasnal.class));
        Submission submission = Submission.newObject(subId, 31, "{test: \"test\"}", SubmissionStatus.Pending, OffsetDateTime.now());

        when(fetch.AddSubmission(newSub)).thenReturn(subId);
        when(fetch.GetSubmission(subId)).thenReturn(submission);

        Submission result = repo.AddSubmission(newSub);

        assertNotNull(result);
        assertSame(submission, result);
        verify(fetch, times(1)).AddSubmission(newSub);
        verify(fetch, times(1)).GetSubmission(subId);
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
    void testUpdateSubmission_callsUpdateAndGetSubmission() {
        int subId = 75;
        NewSubmission newSub = mock(NewSubmission.class);
        Submission submission = Submission.newObject(subId, 23, "{test: \"test\"}", SubmissionStatus.Pending, OffsetDateTime.now());

        when(fetch.UpdateSubmission(subId, newSub)).thenReturn(subId);
        when(fetch.GetSubmission(subId)).thenReturn(submission);

        Submission result = repo.UpdateSubmission(subId, newSub);

        assertNotNull(result);
        assertSame(submission, result);
        verify(fetch, times(1)).UpdateSubmission(subId, newSub);
        verify(fetch, times(1)).GetSubmission(subId);
        verifyNoMoreInteractions(fetch);
    }
}
