package pl.krasmap.interaction.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.krasmap.interaction.application.domain.data.fav.NewVisit;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class HandleVisitedServiceIntegrationTest {

    @Autowired
    private HandleVisitedService handleVisitedService;

    @Test
    void testAddVisit_persistsAndReturnsVisit() {
        // Arrange
        NewVisit newVisit = new NewVisit(1, 1);

        // Act
        VisitedKrasnal added = handleVisitedService.AddVisit(newVisit);
        VisitedKrasnal retrieved = handleVisitedService.GetVisit(added.id());

        // Assert
        assertNotNull(added);
        assertNotNull(retrieved);
        assertEquals(1, retrieved.userId());
        assertEquals(1, retrieved.krasnalId());
    }

    @Test
    void testGetVisit_byUserAndKrasnalId_returnsCorrectVisit() {
        // Arrange
        int userId = 2;
        int krasnalId = 2;
        handleVisitedService.AddVisit(new NewVisit(userId, krasnalId));

        // Act
        VisitedKrasnal retrieved = handleVisitedService.GetVisit(userId, krasnalId);

        // Assert
        assertNotNull(retrieved);
        assertEquals(userId, retrieved.userId());
        assertEquals(krasnalId, retrieved.krasnalId());
    }

    @Test
    void testGetVisitsFromKrasnal_returnsOnlyKrasnalVisits() {
        // Arrange
        int krasnalId = 7;
        handleVisitedService.AddVisit(new NewVisit(1, krasnalId));
        handleVisitedService.AddVisit(new NewVisit(2, krasnalId));
        handleVisitedService.AddVisit(new NewVisit(3, 999));

        // Act
        List<VisitedKrasnal> visits = handleVisitedService.GetVisitsFromKrasnal(krasnalId);

        // Assert
        assertNotNull(visits);
        assertTrue(visits.size() >= 2);
        assertTrue(visits.stream().allMatch(v -> v.krasnalId() == krasnalId));
    }

    @Test
    void testGetVisitsFromUser_returnsOnlyUserVisits() {
        // Arrange
        int userId = 10;
        handleVisitedService.AddVisit(new NewVisit(userId, 1));
        handleVisitedService.AddVisit(new NewVisit(userId, 2));
        handleVisitedService.AddVisit(new NewVisit(999, 3));

        // Act
        List<VisitedKrasnal> visits = handleVisitedService.GetVisitsFromUser(userId);

        // Assert
        assertNotNull(visits);
        assertTrue(visits.size() >= 2);
        assertTrue(visits.stream().allMatch(v -> v.userId() == userId));
    }

    @Test
    void testRemoveVisit_byId_deletesFromDatabase() {
        // Arrange
        VisitedKrasnal added = handleVisitedService.AddVisit(new NewVisit(15, 15));

        // Act
        Boolean removed = handleVisitedService.RemoveVisit(added.id());
        VisitedKrasnal afterRemove = handleVisitedService.GetVisit(added.id());

        // Assert
        assertTrue(removed);
        assertNull(afterRemove);
    }

    @Test
    void testRemoveVisit_byUserAndKrasnalId_deletesFromDatabase() {
        // Arrange
        int userId = 53;
        int krasnalId = 8;
        handleVisitedService.AddVisit(new NewVisit(userId, krasnalId));

        // Act
        Boolean removed = handleVisitedService.RemoveVisit(userId, krasnalId);
        VisitedKrasnal afterRemove = handleVisitedService.GetVisit(userId, krasnalId);

        // Assert
        assertTrue(removed);
        assertNull(afterRemove);
    }
}
