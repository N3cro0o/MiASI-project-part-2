package pl.krasmap.interaction.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krasmap.interaction.application.domain.Review;
import pl.krasmap.interaction.application.domain.ReviewWeb;
import pl.krasmap.interaction.application.port.out.ReviewFetchInterface;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HoldReviewRepoTest {

    @Mock
    private ReviewFetchInterface fetch;

    @InjectMocks
    private HoldReviewRepo repo;

    @Test
    void testGetReviewsUnderKrasnal_callsInterfaceAndReturnsReviewsList() {
        int krasnalId = 101;

        Review r1 = Review.newObject(1, krasnalId, 43, (short)3, "fajny krasnal", OffsetDateTime.now());
        Review r2 = Review.newObject(2, krasnalId, 12, (short)5, "świetny krasnal", OffsetDateTime.now());

        when(fetch.GetReviewsUnderKrasnal(krasnalId)).thenReturn(List.of(r1, r2));

        var result = repo.GetReviewsUnderKrasnal(krasnalId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));
        verify(fetch, times(1)).GetReviewsUnderKrasnal(krasnalId);
    }

    @Test
    void testGetReviewById_callsInterfaceAndReturnsReview() {
        int reviewId = 200;
        Review expectedReview = Review.newObject(reviewId, 10, 30, (short)1, "słaby", OffsetDateTime.now());

        when(fetch.GetReview(reviewId)).thenReturn(expectedReview);

        var result = repo.GetReviewById(reviewId);

        assertNotNull(result);
        assertEquals(expectedReview, result);
        verify(fetch, times(1)).GetReview(reviewId);
    }

    @Test
    void testGetReviewsFromUser_callsInterfaceAndReturnsReviewsList() {
        int userId = 48;
        Review r1 = Review.newObject(1, 23, userId, (short)4, "dobry krasnal", OffsetDateTime.now());
        Review r2 = Review.newObject(523, 12, userId, (short)3, "okej krasnal", OffsetDateTime.now());

        when(fetch.GetReviewsFromUser(userId)).thenReturn(List.of(r1, r2));

        var result = repo.GetReviewsFromUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));
        verify(fetch, times(1)).GetReviewsFromUser(userId);
    }

    @Test
    void testGetReviewsFromUserUnderKrasnal_callsInterfaceAndReturnsReviewsList() {
        int krasnalId = 101;
        int userId = 84;

        Review r1 = Review.newObject(1, krasnalId, userId, (short)4, "dobry krasnal", OffsetDateTime.now());
        Review r2 = Review.newObject(523, krasnalId, userId, (short)3, "okej krasnal", OffsetDateTime.now());

        when(fetch.GetReviewsFromUserUnderKrasnal(userId, krasnalId)).thenReturn(List.of(r1, r2));

        var result = repo.GetReviewsFromUserUnderKrasnal(userId, krasnalId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));
        verify(fetch, times(1)).GetReviewsFromUserUnderKrasnal(userId, krasnalId);
    }

    @Test
    void testAddReview_callsAddAndGet() {
        ReviewWeb review = new ReviewWeb(14, 43, (short)2, "mógłby być lepszy");
        int generatedId = 1;
        Review returnedReview = Review.newObject(1, 14, 43, (short)2, "mógłby być lepszy", OffsetDateTime.now());

        when(fetch.AddReview(review)).thenReturn(generatedId);
        when(fetch.GetReview(generatedId)).thenReturn(returnedReview);

        var result = repo.AddReview(review);

        assertNotNull(result);
        assertEquals(generatedId, result.id());
        assertEquals(returnedReview, result);
        verify(fetch, times(1)).AddReview(review);
        verify(fetch, times(1)).GetReview(generatedId);
    }

    @Test
    void testUpdateReview_callsUpdateAndGet() {
        int reviewId = 240;
        ReviewWeb review = new ReviewWeb(43, 4, (short)5, "naprawili krasnala");
        Review returnedReview = Review.newObject(reviewId, 43, 4, (short)5, "naprawili krasnala", OffsetDateTime.now());

        when(fetch.UpdateReview(reviewId, review)).thenReturn(reviewId);
        when(fetch.GetReview(reviewId)).thenReturn(returnedReview);

        var result = repo.UpdateReview(reviewId, review);

        assertNotNull(result);
        assertEquals(reviewId, result.id());
        assertEquals(returnedReview, result);
        verify(fetch, times(1)).UpdateReview(reviewId, review);
        verify(fetch, times(1)).GetReview(reviewId);
    }

    @Test
    void testRemoveReview_callsRemoveReviewAndReturnsTrue() {
        int reviewId = 465;
        when(fetch.RemoveReview(reviewId)).thenReturn(true);

        boolean result = repo.RemoveReview(reviewId);

        assertTrue(result);
        verify(fetch, times(1)).RemoveReview(reviewId);
    }

    @Test
    void testRemoveReview_callsRemoveReviewAndReturnsFalse() {
        int reviewId = 465;
        when(fetch.RemoveReview(reviewId)).thenReturn(false);

        boolean result = repo.RemoveReview(reviewId);

        assertFalse(result);
        verify(fetch, times(1)).RemoveReview(reviewId);
    }
}
