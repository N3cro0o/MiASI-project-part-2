package pl.krasmap.interaction.application.service;

import org.junit.jupiter.api.Test;
import pl.krasmap.interaction.application.domain.Review;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

public class HoldReviewRepoTest {

    @Test
    void testGetReviewList_emptyInitially() {
        HoldReviewRepo repo = new HoldReviewRepo();

        List<Review> result = repo.getReviewList();

        assertNotNull(result, "getReviewList() should not return null");
        assertTrue(result.isEmpty(), "LIst should be empty initially");
    }

    @Test
    void testGetReviewList_returnsAddedReviews() {
        HoldReviewRepo repo = new HoldReviewRepo();
        LocalDateTime now = LocalDateTime.now();

        List<Review> list = repo.getReviewList();
        Review r1 = new Review(1, 101, 11, (short)3, "średnie", now);
        Review r2 = new Review(2, 202, 22, (short)5, "Mocne", now);

        list.add(r1);
        list.add(r2);

        List<Review> result = repo.getReviewList();

        assertEquals(2, result.size(), "List should include two reviews");
        assertTrue(result.contains(r1), "List should contain review r1");
        assertTrue(result.contains(r2), "List should contain review r2");
    }

    @Test
    void testGetReviewList_byKrasnalId_filtersCorrectly() {
        HoldReviewRepo repo = new HoldReviewRepo();
        LocalDateTime now = LocalDateTime.now();

        List<Review> list = repo.getReviewList();
        list.add(new Review(1, 101, 11, (short)3, "średnie", now));
        list.add(new Review(2, 202, 22, (short)5, "Mocne", now));
        list.add(new Review(3, 303, 24, (short)1, "Słabe", now));
        list.add(new Review(4, 101, 5, (short)4, "Fajne", now));
        list.add(new Review(5, 202, 6, (short)2, "Może być", now));

        List<Review> result1 = repo.getReviewList(101);
        List<Review> result2 = repo.getReviewList(303);
        List<Review> result3 = repo.getReviewList(404);

        assertEquals(2, result1.size(), "There should be two reviews for krasnalId = 101");
        assertTrue(result1.stream().allMatch(r -> r.krasnalId() == 101),
                "All reviews for krasnalId = 101 should have krasnalId = 101");

        assertEquals(1, result2.size(), "There should be one review for krasnalId = 303");
        assertTrue(result2.stream().allMatch(r -> r.krasnalId() == 303),
                "All reviews for krasnalId = 303 should have krasnalId = 303");

        assertEquals(0, result3.size(), "There should be zero reviews for krasnalId = 404");
    }
}
