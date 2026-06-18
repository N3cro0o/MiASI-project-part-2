package pl.krasmap.interaction.application.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReviewUserTest {

    @Test
    void testReviewUserCreation_fieldsSetCorrectly() {
        ReviewUser user = new ReviewUser(1, "debil@pwr.edu.pl");

        assertEquals(1, user.id());
        assertEquals("debil@pwr.edu.pl", user.email());
    }
}
