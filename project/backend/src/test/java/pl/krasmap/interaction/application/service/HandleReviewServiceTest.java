package pl.krasmap.interaction.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.krasmap.interaction.application.domain.data.review.Review;
import pl.krasmap.interaction.application.domain.data.review.ReviewWeb;
import pl.krasmap.interaction.application.domain.event.review.ReviewAddedEvent;
import pl.krasmap.interaction.application.domain.event.review.ReviewDeleteEvent;
import pl.krasmap.interaction.application.domain.event.review.ReviewUpdatedEvent;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HandleReviewServiceTest {

    @Mock
    private HoldReviewRepo repo;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private HandleReviewService handleReviewService;

    @Test
    void testGetReviewsUnderKrasnal_CallRepoAndReturnReviewList() {
        int krasnalId = 83;
        int reviewId = 1;
        int userId = 732;
        List<Review> reviews = List.of(
                Review.newObject(reviewId, krasnalId, userId, (short)4, "content", OffsetDateTime.now()),
                mock(Review.class)
        );

        when(repo.GetReviewsUnderKrasnal(krasnalId)).thenReturn(reviews);

        List<Review> result = handleReviewService.GetReviewsUnderKrasnal(krasnalId);

        assertNotNull(result);
        assertEquals(reviews, result);
        assertEquals(2, result.size());
        assertEquals(reviewId, result.get(0).id());
        verify(repo, times(1)).GetReviewsUnderKrasnal(krasnalId);
    }

    @Test
    void testGetReviewById_CallRepoAndReturnReview() {
        int reviewId = 1;
        Review review = Review.newObject(1, 342, 39, (short)3, "content", OffsetDateTime.now());

        when(repo.GetReviewById(reviewId)).thenReturn(review);

        Review result = handleReviewService.GetReviewById(reviewId);

        assertNotNull(result);
        assertEquals(review, result);
        verify(repo, times(1)).GetReviewById(reviewId);
    }

    @Test
    void testGetReviewsFromUser_CallRepoAndReturnReviewList() {
        int userId = 823;

        List<Review> reviews = List.of(
                mock(Review.class),
                mock(Review.class)
        );

        when(repo.GetReviewsFromUser(userId)).thenReturn(reviews);

        List<Review> result = handleReviewService.GetReviewsFromUser(userId);

        assertNotNull(result);
        assertEquals(reviews, result);
        assertEquals(2, result.size());
        verify(repo, times(1)).GetReviewsFromUser(userId);
    }

    @Test
    void testGetReviewsFromUserUnderKrasnal_CallRepoAndReturnReviewList() {
        int userId = 23;
        int krasnalId = 83;

        List<Review> reviews = List.of(
                mock(Review.class),
                mock(Review.class)
        );

        when(repo.GetReviewsUnderKrasnal(krasnalId)).thenReturn(reviews);

        List<Review> result = handleReviewService.GetReviewsUnderKrasnal(krasnalId);

        assertNotNull(result);
        assertEquals(reviews, result);
        assertEquals(2, result.size());
        verify(repo, times(1)).GetReviewsUnderKrasnal(krasnalId);
    }

    @Test
    void testAddReview_AddReviewToRepoAndPublishEvent() {
        ReviewWeb reviewToAdd = new ReviewWeb(12, 32, (short)4, "content");
        int id = 3;
        Review review = Review.newObject(id, 12, 32, (short)4, "content", OffsetDateTime.now());

        when(repo.AddReview(reviewToAdd)).thenReturn(id);
        when(handleReviewService.GetReviewById(id)).thenReturn(review);

        Review result = handleReviewService.AddReview(reviewToAdd);

        assertNotNull(result);
        assertEquals(result, review);
        verify(repo, times(1)).AddReview(reviewToAdd);
        verify(events, times(1)).publishEvent(any(ReviewAddedEvent.class));
    }

    @Test
    void testUpdateReview_UpdateReviewInRepoAndPublishEvent() {
        int reviewId = 17;
        ReviewWeb reviewToUpdate = new ReviewWeb(3, 5, (short)4, "content");
        Review review = Review.newObject(reviewId, 3, 5, (short)4, "content", OffsetDateTime.now());

        when(handleReviewService.GetReviewById(reviewId)).thenReturn(review);

        Review result = handleReviewService.UpdateReview(reviewId, reviewToUpdate);

        assertNotNull(result);
        assertEquals(result, review);
        verify(repo, times(1)).UpdateReview(reviewId, reviewToUpdate);
        verify(events, times(1)).publishEvent(any(ReviewUpdatedEvent.class));
    }

    @Test
    void testRemoveReview_RemoveReviewFromRepoAndPublishEvent() {
        int reviewId = 17;
        Review review = Review.newObject(reviewId, 3, 5, (short)4, "content", OffsetDateTime.now());

        when(handleReviewService.GetReviewById(reviewId)).thenReturn(review);
        when(repo.RemoveReview(reviewId)).thenReturn(true);

        boolean result = handleReviewService.RemoveReview(reviewId);

        assertTrue(result);
        verify(repo, times(1)).RemoveReview(reviewId);
        verify(events, times(1)).publishEvent(any(ReviewDeleteEvent.class));
    }
}
