package pl.krasmap.submission.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.domain.data.NewSubmission;
import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.submission.application.domain.event.SubCreatedEvent;
import pl.krasmap.submission.application.domain.event.SubUpdatedEvent;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HandleSubmissionServiceTest {

    @Mock
    private HoldSubmissionRepo repo;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private HandleSubmissionService handleSubmissionService;

    @Test
    void testAddSubmission_AddSubmissionToRepoAndPublishEvent() {
        int userId = 83;
        int subId = 82;
        NewSubmission newSub = new NewSubmission(userId, mock(ReviewKrasnal.class));
        Submission submission = Submission.newObject(subId, userId, "{test: \"test\"}", SubmissionStatus.Pending, OffsetDateTime.now());

        when(repo.AddSubmission(newSub)).thenReturn(subId);
        when(handleSubmissionService.GetSubmission(subId)).thenReturn(submission);

        Submission result = handleSubmissionService.AddSubmission(newSub);

        assertNotNull(result);
        assertEquals(submission, result);
        verify(repo, times(1)).AddSubmission(newSub);
        verify(events, times(1)).publishEvent(any(SubCreatedEvent.class));
    }

    @Test
    void testCheckSubmission_CallRepoAndReturnSubmissionStatus() {
        int subId = 73;
        SubmissionStatus subStat = SubmissionStatus.Pending;

        when(repo.CheckSubmission(subId)).thenReturn(subStat);

        SubmissionStatus result = handleSubmissionService.CheckSubmission(subId);

        assertNotNull(result);
        assertEquals(subStat, result);
        verify(repo, times(1)).CheckSubmission(subId);
    }

    @Test
    void testGetSubmissionsFromUser_CallRepoAndReturnSubmissionList() {
        int subId = 73;
        int userId = 84;
        List<Submission> userSubs = List.of(
                Submission.newObject(subId, userId, "{test: \"test\"}", SubmissionStatus.Pending, OffsetDateTime.now()),
                mock(Submission.class),
                mock(Submission.class)
        );

        when(repo.GetSubmissionsFromUser(userId)).thenReturn(userSubs);

        List<Submission> result = handleSubmissionService.GetSubmissionsFromUser(userId);

        assertNotNull(result);
        assertEquals(userSubs, result);
        assertEquals(3, result.size());
        assertEquals(subId, result.get(0).id());
        verify(repo, times(1)).GetSubmissionsFromUser(userId);
    }

    @Test
    void testGetSubmission_CallRepoAndReturnSubmission() {
        int subId = 92;
        Submission submission = Submission.newObject(subId, 23, "{test: \"test\"}", SubmissionStatus.Pending, OffsetDateTime.now());

        when(repo.GetSubmission(subId)).thenReturn(submission);

        Submission result = handleSubmissionService.GetSubmission(subId);

        assertNotNull(result);
        assertEquals(submission, result);
        verify(repo, times(1)).GetSubmission(subId);
    }

    @Test
    void testUpdateSubReview_UpdateReviewInRepo() {
        Submission newSub = Submission.newObject(75, 23, "{test: \"test\"}", SubmissionStatus.Pending, OffsetDateTime.now());

        when(repo.UpdateSubReview(newSub)).thenReturn(true);

        boolean result = handleSubmissionService.UpdateSubReview(newSub);

        assertTrue(result);
        verify(repo, times(1)).UpdateSubReview(newSub);
    }

    @Test
    void testUpdateSubmission_UpdateSubmissionInRepoAndPublishEvent() {
        int subId = 73;
        NewSubmission newSub = new NewSubmission(subId, mock(ReviewKrasnal.class));
        Submission submission = Submission.newObject(subId, 43, "{test: \"test\"}", SubmissionStatus.Pending, OffsetDateTime.now());

        when(repo.UpdateSubmission(subId, newSub)).thenReturn(subId);
        when(repo.GetSubmission(subId)).thenReturn(submission);

        Submission result = handleSubmissionService.UpdateSubmission(subId, newSub);

        assertNotNull(result);
        assertEquals(submission, result);
        verify(repo, times(1)).UpdateSubmission(subId, newSub);
        verify(repo, times(1)).GetSubmission(subId);
        verify(events, times(1)).publishEvent(any(SubUpdatedEvent.class));
    }

    @Test
    void testGetAllSubmissions_callRepoAndReturnSubmissionList() {
        int firstSubId = 73;
        List<Submission> submissions = List.of(
                Submission.newObject(firstSubId, 32, "{test: \"test\"}", SubmissionStatus.Pending, OffsetDateTime.now()),
                mock(Submission.class),
                mock(Submission.class)
        );

        when(repo.GetAllSubmissions()).thenReturn(submissions);

        List<Submission> result = handleSubmissionService.GetAllSubmissions();

        assertNotNull(result);
        assertEquals(submissions, result);
        assertEquals(3, result.size());
        assertEquals(firstSubId, result.get(0).id());
        verify(repo, times(1)).GetAllSubmissions();
    }

    @Test
    void testGetAllSubmissionsByStatus_callRepoAndReturnSubmissionList() {
        int firstSubId = 73;
        SubmissionStatus status = SubmissionStatus.Rejected;
        List<Submission> submissions = List.of(
                Submission.newObject(firstSubId, 32, "{test: \"test\"}", SubmissionStatus.Rejected, OffsetDateTime.now()),
                mock(Submission.class),
                mock(Submission.class)
        );

        when(repo.GetAllSubmissions(status)).thenReturn(submissions);

        List<Submission> result = handleSubmissionService.GetAllSubmissions(status);

        assertNotNull(result);
        assertEquals(submissions, result);
        assertEquals(3, result.size());
        assertEquals(firstSubId, result.get(0).id());
        verify(repo, times(1)).GetAllSubmissions(status);
    }
}
