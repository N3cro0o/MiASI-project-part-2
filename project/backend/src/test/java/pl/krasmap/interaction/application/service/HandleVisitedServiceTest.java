package pl.krasmap.interaction.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.krasmap.interaction.application.domain.data.fav.NewVisit;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;
import pl.krasmap.interaction.application.domain.event.fav.VisitedAddedEvent;
import pl.krasmap.interaction.application.domain.event.fav.VisitedDeletedEvent;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HandleVisitedServiceTest {

    @Mock
    private HoldVisitedRepo repo;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private HandleVisitedService handleVisitedService;

    @Test
    void testGetVisitById_CallRepoAndReturnVisit() {
        int visitedId = 1;
        VisitedKrasnal visit = mock(VisitedKrasnal.class);

        when(repo.GetVisit(visitedId)).thenReturn(visit);

        VisitedKrasnal result = handleVisitedService.GetVisit(visitedId);

        assertNotNull(result);
        assertEquals(visit, result);
        verify(repo, times(1)).GetVisit(visitedId);
    }

    @Test
    void testGetVisitByUserIdAndKrasnalId_CallRepoAndReturnVisit() {
        int userId = 42;
        int krasnalId = 83;
        VisitedKrasnal visit = mock(VisitedKrasnal.class);

        when(repo.GetVisit(userId, krasnalId)).thenReturn(visit);

        VisitedKrasnal result = handleVisitedService.GetVisit(userId, krasnalId);

        assertNotNull(result);
        assertEquals(visit, result);
        verify(repo, times(1)).GetVisit(userId, krasnalId);
    }

    @Test
    void testGetVisitsFromKrasnal_CallRepoAndReturnVisitList() {
        int krasnalId = 83;
        List<VisitedKrasnal> visits = List.of(
                mock(VisitedKrasnal.class),
                mock(VisitedKrasnal.class)
        );

        when(repo.GetVisitsFromKrasnal(krasnalId)).thenReturn(visits);

        List<VisitedKrasnal> result = handleVisitedService.GetVisitsFromKrasnal(krasnalId);

        assertNotNull(result);
        assertEquals(visits, result);
        assertEquals(2, result.size());
        verify(repo, times(1)).GetVisitsFromKrasnal(krasnalId);
    }

    @Test
    void testGetVisitsFromUser_CallRepoAndReturnVisitList() {
        int userId = 732;
        List<VisitedKrasnal> visits = List.of(
                mock(VisitedKrasnal.class),
                mock(VisitedKrasnal.class)
        );

        when(repo.GetVisitsFromUser(userId)).thenReturn(visits);

        List<VisitedKrasnal> result = handleVisitedService.GetVisitsFromUser(userId);

        assertNotNull(result);
        assertEquals(visits, result);
        assertEquals(2, result.size());
        verify(repo, times(1)).GetVisitsFromUser(userId);
    }

    @Test
    void testAddVisit_AddVisitToRepoAndPublishEvent() {
        int visitedId = 5;
        NewVisit newVisit = new NewVisit(83, 42);
        VisitedKrasnal visit = mock(VisitedKrasnal.class);

        when(repo.AddVisit(newVisit)).thenReturn(visitedId);
        when(repo.GetVisit(visitedId)).thenReturn(visit);

        VisitedKrasnal result = handleVisitedService.AddVisit(newVisit);

        assertNotNull(result);
        assertEquals(visit, result);
        verify(repo, times(1)).AddVisit(newVisit);
        verify(events, times(1)).publishEvent(any(VisitedAddedEvent.class));
    }

    @Test
    void testRemoveVisitById_RemoveVisitFromRepoAndPublishEvent() {
        int visitedId = 5;
        VisitedKrasnal visit = mock(VisitedKrasnal.class);

        when(repo.GetVisit(visitedId)).thenReturn(visit);
        when(repo.RemoveVisit(visitedId)).thenReturn(true);

        Boolean result = handleVisitedService.RemoveVisit(visitedId);

        assertTrue(result);
        verify(repo, times(1)).RemoveVisit(visitedId);
        verify(events, times(1)).publishEvent(any(VisitedDeletedEvent.class));
    }

    @Test
    void testRemoveVisitByUserIdAndKrasnalId_RemoveVisitFromRepoAndPublishEvent() {
        int userId = 42;
        int krasnalId = 83;
        VisitedKrasnal visit = mock(VisitedKrasnal.class);

        when(repo.GetVisit(userId, krasnalId)).thenReturn(visit);
        when(repo.RemoveVisit(userId, krasnalId)).thenReturn(true);

        Boolean result = handleVisitedService.RemoveVisit(userId, krasnalId);

        assertTrue(result);
        verify(repo, times(1)).RemoveVisit(userId, krasnalId);
        verify(events, times(1)).publishEvent(any(VisitedDeletedEvent.class));
    }
}
