package pl.krasmap.submission.application.service;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
//import pl.krasmap.submission.application.domain.Krasnal;
import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.submission.application.domain.data.submission.SubmissionReview;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.domain.service.CheckSubmission;
import pl.krasmap.submission.application.port.out.GetKrasnalInterface;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckSubmissionTest {

    @Mock
    private HandleSubmissionService subHandle;

    @Mock
    private GetKrasnalInterface krasnalService;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private CheckSubmission check;

    @Captor
    private ArgumentCaptor<Submission> subCaptor;

    private static final String SAMPLE_JSON = """
            {
                "name": "krasnal student",
                "description": "krasnal student z piwem",
                "position": {
                    "latitude": 43.22,
                    "longitude": 54.34
                },
                "category": "Dwarf"
            }
            """;

    @Test
    void testGetSubmissionPair_returnsPairWithParsedKrasnal() {
        int subId = 3;
        Submission sub = Submission.newObject(subId, 2, SAMPLE_JSON, SubmissionStatus.Pending, OffsetDateTime.now());

        when(subHandle.GetSubmission(subId)).thenReturn(sub);

        Pair<Submission, ReviewKrasnal> pair = check.GetSubmissonPair(subId);

        assertNotNull(pair);
        assertSame(sub, pair.getLeft());

        ReviewKrasnal k = pair.getRight();

        assertNotNull(k);
        assertEquals("krasnal student", k.name());
        assertEquals("krasnal student z piwem", k.description());
        assertEquals(new Position(43.22, 54.34), k.position());
        assertEquals(KrasnalCategory.Dwarf, k.category());
        verify(subHandle, times(1)).GetSubmission(subId);
    }

    @Test
    void testGenerateKrasnalFromJson_whenInvalidJson_returnsNull() {
        ReviewKrasnal k = check.GenerateKrasnalFromJson("this is not a valid json");
        assertNull(k);
    }

    @Test
    void testRejectSubmission_createsReviewAndCallsUpdate() {
        int userId = 33;
        int subId = 23;
        String reason = "krasnal doesn't exist";
        Submission sub = Submission.newObject(subId, 4, SAMPLE_JSON, SubmissionStatus.Pending, OffsetDateTime.now());

        when(subHandle.GetSubmission(subId)).thenReturn(sub);
        when(subHandle.UpdateSubReview(any(Submission.class))).thenReturn(false);

        boolean result = check.RejectSubmission(userId, subId, reason);

        assertFalse(result);
        verify(subHandle, times(1)).GetSubmission(subId);
        verify(subHandle, times(1)).UpdateSubReview(subCaptor.capture());

        Submission updatedSub = subCaptor.getValue();
        assertNotNull(updatedSub);
        assertEquals(SubmissionStatus.Rejected, updatedSub.status());

        SubmissionReview review = updatedSub.review();
        assertNotNull(review);
        assertEquals(userId, review.reviewerId());
        assertEquals(reason, review.rejectionReason());
    }

    @Test
    void testAcceptSubmission_updatesAndReturnsKrasnal() {
        int userId = 54;
        int subId = 98;
        Submission sub = Submission.newObject(subId, 2, SAMPLE_JSON, SubmissionStatus.Pending, OffsetDateTime.now());

        when(subHandle.GetSubmission(subId)).thenReturn(sub);
        when(subHandle.UpdateSubReview(any(Submission.class))).thenReturn(false);

        ReviewKrasnal returned = check.AcceptSubmission(userId, subId);

        assertNotNull(returned);
        assertEquals("krasnal student", returned.name());
        assertEquals("krasnal student z piwem", returned.description());

        verify(subHandle, times(1)).GetSubmission(subId);
        verify(subHandle, times(1)).UpdateSubReview(subCaptor.capture());

        Submission newSub = subCaptor.getValue();
        assertNotNull(newSub);
        assertEquals(SubmissionStatus.Accepted, newSub.status());

        SubmissionReview review = newSub.review();
        assertNotNull(review);
        assertEquals(userId, review.reviewerId());
        assertEquals("Accepted", review.rejectionReason());
    }
}
