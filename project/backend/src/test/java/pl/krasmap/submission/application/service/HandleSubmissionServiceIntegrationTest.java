package pl.krasmap.submission.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.domain.data.NewSubmission;
import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.data.submission.Submission;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class HandleSubmissionServiceIntegrationTest {

    @Autowired
    private HandleSubmissionService handleSubmissionService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Test
    void testAddSubmission_persistsAndPublishesEvent() {
        NewSubmission newSub = new NewSubmission(
                1,
                new ReviewKrasnal(
                        "test krasnal",
                        "testowy opis",
                        new Position(50.0, 20.0),
                        KrasnalCategory.Dwarf
                )
        );

        Submission added = handleSubmissionService.AddSubmission(newSub);
        Submission retrieved = handleSubmissionService.GetSubmission(added.id());

        assertNotNull(added);
        assertNotNull(retrieved);
        assertEquals(1, retrieved.userId());
        assertEquals(SubmissionStatus.Pending, retrieved.status());
    }

    @Test
    void testUpdateSubmission_changesStatusInDatabase() {
        NewSubmission newSub = new NewSubmission(
                2,
                new ReviewKrasnal(
                        "test krasnal",
                        "testowy opis",
                        new Position(50.0, 20.0),
                        KrasnalCategory.Dwarf
                )
        );
        Submission added = handleSubmissionService.AddSubmission(newSub);

        NewSubmission updatedSub = new NewSubmission(
                2,
                new ReviewKrasnal(
                        "test krasnal",
                        "zaktualizowany opis",
                        new Position(52.0, 25.0),
                        KrasnalCategory.Dwarf
                )
        );

        Submission updated = handleSubmissionService.UpdateSubmission(added.id(), updatedSub);
        Submission retrieved = handleSubmissionService.GetSubmission(added.id());

        assertNotNull(updated);
        assertEquals(2, retrieved.userId());
    }

    @Test
    void testGetSubmissionsFromUser_returnsOnlyUserSubmissions() {
        int userId = 4;
        NewSubmission sub1 = new NewSubmission(
                userId,
                new ReviewKrasnal(
                        "sub1",
                        "sub1 opis",
                        new Position(53.0, 53.0),
                        KrasnalCategory.Flora
                )
        );
        NewSubmission sub2 = new NewSubmission(
                userId,
                new ReviewKrasnal(
                        "sub2",
                        "sub2 opis",
                        new Position(54.0, 54.0),
                        KrasnalCategory.Building
                )
        );
        NewSubmission sub3 = new NewSubmission(
                976,
                new ReviewKrasnal(
                        "sub3",
                        "sub3 opis",
                        new Position(55.0, 55.0),
                        KrasnalCategory.Flora
                )
        );

        handleSubmissionService.AddSubmission(sub1);
        handleSubmissionService.AddSubmission(sub2);
        handleSubmissionService.AddSubmission(sub3);

        List<Submission> userSubs = handleSubmissionService.GetSubmissionsFromUser(userId);

        assertNotNull(userSubs);
        assertEquals(2, userSubs.size());
        assertTrue(userSubs.stream().allMatch(s -> s.userId() == userId));
    }

    @Test
    void testGetAllSubmissions_returnsAllSubmissions() {
        handleSubmissionService.AddSubmission(new NewSubmission(
                1,
                new ReviewKrasnal(
                        "krasnal1",
                        "opis1",
                        new Position(30.4, 12.8),
                        KrasnalCategory.Dwarf
                )
        ));
        handleSubmissionService.AddSubmission(new NewSubmission(
                2,
                new ReviewKrasnal(
                        "krasnal2",
                        "opis2",
                        new Position(43.4, 5.43),
                        KrasnalCategory.Monument
                )
        ));

        List<Submission> all = handleSubmissionService.GetAllSubmissions();

        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }

    @Test
    void testGetAllSubmissions_withStatus_filtersCorrectly() {
        Submission sub = handleSubmissionService.AddSubmission(new NewSubmission(
                2,
                new ReviewKrasnal(
                        "testkrasnal3",
                        "test opis",
                        new Position(23.0, 45.0),
                        KrasnalCategory.Dwarf
                )
        ));

        List<Submission> pending = handleSubmissionService.GetAllSubmissions(SubmissionStatus.Pending);

        assertNotNull(pending);
        assertTrue(pending.stream().anyMatch(s -> s.id() == sub.id()));
    }

    @Test
    void testCheckSubmission_returnsCorrectStatus() {
        Submission sub = handleSubmissionService.AddSubmission(new NewSubmission(
                1,
                new ReviewKrasnal(
                        "testkrasnal4",
                        "test opis",
                        new Position(53.0, 53.0),
                        KrasnalCategory.Dwarf
                )
        ));

        SubmissionStatus status = handleSubmissionService.CheckSubmission(sub.id());

        assertNotNull(status);
        assertEquals(SubmissionStatus.Pending, status);
    }
}
