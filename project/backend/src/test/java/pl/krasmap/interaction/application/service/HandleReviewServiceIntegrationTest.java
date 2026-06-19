package pl.krasmap.interaction.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.krasmap.interaction.application.domain.data.review.Review;
import pl.krasmap.interaction.application.domain.data.review.ReviewWeb;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class HandleReviewServiceIntegrationTest {

    @Autowired
    private HandleReviewService handleReviewService;

    @Test
    void testAddReview_persistsAndReturnsReview() {
        // Arrange
        ReviewWeb newReview = new ReviewWeb(1, 1, (short)5, "dobry krasnal");

        // Act
        Review added = handleReviewService.AddReview(newReview);
        Review retrieved = handleReviewService.GetReviewById(added.id());

        // Assert
        assertNotNull(added);
        assertNotNull(retrieved);
        assertEquals(1, retrieved.userId());
        assertEquals(1, retrieved.krasnalId());
        assertEquals("dobry krasnal", retrieved.content());
    }

    @Test
    void testGetReviewsUnderKrasnal_returnsOnlyKrasnalReviews() {
        // Arrange
        int krasnalId = 5;
        handleReviewService.AddReview(new ReviewWeb(krasnalId, 1, (short)5, "fajny"));
        handleReviewService.AddReview(new ReviewWeb(krasnalId, 2, (short)4, "dobry"));
        handleReviewService.AddReview(new ReviewWeb(99, 3, (short)3, "inny krasnal"));

        // Act
        List<Review> reviews = handleReviewService.GetReviewsUnderKrasnal(krasnalId);

        // Assert
        assertNotNull(reviews);
        assertEquals(2, reviews.size());
        assertTrue(reviews.stream().allMatch(r -> r.krasnalId() == krasnalId));
    }

    @Test
    void testGetReviewsFromUser_returnsOnlyUserReviews() {
        // Arrange
        int userId = 7;
        handleReviewService.AddReview(new ReviewWeb(5, userId, (short)5, "review1"));
        handleReviewService.AddReview(new ReviewWeb(6, userId, (short)4, "review2"));
        handleReviewService.AddReview(new ReviewWeb(7, 999, (short)3, "inny user"));

        // Act
        List<Review> reviews = handleReviewService.GetReviewsFromUser(userId);

        // Assert
        assertNotNull(reviews);
        assertEquals(2, reviews.size());
        assertTrue(reviews.stream().allMatch(r -> r.userId() == userId));
    }

    @Test
    void testGetReviewsFromUserUnderKrasnal_filtersCorrectly() {
        // Arrange
        int userId = 8;
        int krasnalId = 10;
        handleReviewService.AddReview(new ReviewWeb(krasnalId, userId, (short)5, "review1"));
        handleReviewService.AddReview(new ReviewWeb(999, userId, (short)4, "review2"));
        handleReviewService.AddReview(new ReviewWeb(krasnalId, 999, (short)3, "review3"));

        // Act
        List<Review> reviews = handleReviewService.GetReviewsFromUserUnderKrasnal(userId, krasnalId);

        // Assert
        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(userId, reviews.get(0).userId());
        assertEquals(krasnalId, reviews.get(0).krasnalId());
    }

    @Test
    void testUpdateReview_changesReviewInDatabase() {
        // Arrange
        Review added = handleReviewService.AddReview(new ReviewWeb(
                15, 1, (short)5, "stara opinia")
        );
        ReviewWeb updated = new ReviewWeb(15, 1, (short)3, "nowa opinia");

        // Act
        Review reviewAfterUpdate = handleReviewService.UpdateReview(added.id(), updated);
        Review retrieved = handleReviewService.GetReviewById(added.id());

        // Assert
        assertNotNull(retrieved);
        assertEquals("nowa opinia", retrieved.content());
    }

    @Test
    void testRemoveReview_deletesReviewFromDatabase() {
        // Arrange
        Review added = handleReviewService.AddReview(new ReviewWeb(
                16, 1, (short)5, "do usunięcia")
        );

        // Act
        boolean removed = handleReviewService.RemoveReview(added.id());
        Review afterRemove = handleReviewService.GetReviewById(added.id());

        // Assert
        assertTrue(removed);
        assertNull(afterRemove);
    }
}
