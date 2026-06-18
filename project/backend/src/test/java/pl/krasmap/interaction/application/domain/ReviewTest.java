package pl.krasmap.interaction.application.domain;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewTest {

    @Test
    void testReviewCreation_fieldsSetCorrectly() {
        Review review = new Review(1, 101, 5, (short)4, "Recenzja", OffsetDateTime.parse("2026-06-01T12:00:00+00:00"));

        assertEquals(1, review.id());
        assertEquals(101, review.krasnalId());
        assertEquals(5, review.userId());
        assertEquals((short)4, review.rating());
        assertEquals("Recenzja", review.content());
        assertEquals(OffsetDateTime.parse("2026-06-01T12:00:00+00:00"), review.created());
    }
}
