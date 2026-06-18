package pl.krasmap.krasnal.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.krasnal.application.domain.data.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.data.krasnal.Krasnal;
import pl.krasmap.krasnal.application.domain.data.krasnal.KrasnalStatus;
import pl.krasmap.krasnal.application.domain.event.KrasnalAddedEvent;
import pl.krasmap.krasnal.application.domain.event.KrasnalDestroyedEvent;
import pl.krasmap.krasnal.application.domain.event.KrasnalHiddenEvent;
import pl.krasmap.krasnal.application.domain.event.KrasnalUpdatedEvent;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KrasnalHandleServiceTest {

    @Mock
    private HoldKrasnalRepo repo;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private KrasnalHandleService krasnalHandleService;

    @Test
    void testGetKrasnal_CallRepoAndReturnKrasnal() {
        int id = 83;
        Krasnal krasnal = Krasnal.newObject(id, "krasnal student", "krasnal student", new Position(43.55, 12.44));

        when(repo.GetKrasnal(id)).thenReturn(krasnal);

        Krasnal result = krasnalHandleService.GetKrasnal(id);

        assertNotNull(result);
        assertEquals(krasnal, result);
        verify(repo, times(1)).GetKrasnal(id);
    }

    @Test
    void testGetKrasnalList_CallRepoAndReturnKrasnalList() {
        int firstKrasnalId = 84;
        List<Krasnal> krasnals = List.of(
                Krasnal.newObject(firstKrasnalId, "krasnal student", "krasnal student", new Position(32.4, 45.3)),
                mock(Krasnal.class),
                mock(Krasnal.class)
        );

        when(repo.GetKrasnalList()).thenReturn(krasnals);

        List<Krasnal> result = krasnalHandleService.GetKrasnalList();

        assertNotNull(result);
        assertEquals(krasnals, result);
        assertEquals(3, result.size());
        assertEquals(firstKrasnalId, result.get(0).id());
        verify(repo, times(1)).GetKrasnalList();
    }

    @Test
    void testAddNewKrasnal_addKrasnalToRepoAndPublishEvent() {
        int krasnalId = 37;
        KrasnalWeb newKrasnal = new KrasnalWeb("krasnal student", "krasnal student", new Position(12.3, 54.32), KrasnalCategory.Dwarf, KrasnalStatus.Active, 4.33);
        Krasnal toAdd = Krasnal.from(newKrasnal);

        when(repo.AddNewKrasnal(toAdd)).thenReturn(krasnalId);
        when(krasnalHandleService.GetKrasnal(krasnalId)).thenReturn(toAdd);

        Krasnal result = krasnalHandleService.AddNewKrasnal(newKrasnal);

        assertNotNull(result);
        assertEquals(result, toAdd);
        verify(repo, times(1)).AddNewKrasnal(toAdd);
        verify(events, times(1)).publishEvent(any(KrasnalAddedEvent.class));
    }

    @Test
    void testUpdateKrasnal_updateKrasnalInRepoAndPublishEvent() {
        int krasnalId = 37;
        KrasnalWeb krasnalToUpdate = new KrasnalWeb("krasnal student", "krasnal student", new Position(12.3, 54.32), KrasnalCategory.Dwarf, KrasnalStatus.Active, 4.33);
        Krasnal toAdd = Krasnal.from(krasnalId, krasnalToUpdate);

        when(krasnalHandleService.GetKrasnal(krasnalId)).thenReturn(toAdd);

        Krasnal result = krasnalHandleService.UpdateKrasnal(krasnalId, krasnalToUpdate);

        assertNotNull(result);
        assertEquals(toAdd, result);
        verify(repo, times(1)).UpdateKrasnal(toAdd);
        verify(events, times(1)).publishEvent(any(KrasnalUpdatedEvent.class));
    }

    @Test
    void testHideKrasnal_HideKrasnalInRepoAndPublishEvent() {
        int krasnalId = 32;
        Krasnal krasnal = Krasnal.newObject(krasnalId, "krasnal student", "krasnal student", new Position(32.4, 45.3));

        when(krasnalHandleService.GetKrasnal(krasnalId)).thenReturn(krasnal);
        when(repo.HideKrasnal(krasnalId)).thenReturn(true);

        boolean result = krasnalHandleService.HideKrasnal(krasnalId);

        assertTrue(result);
        verify(repo, times(1)).HideKrasnal(krasnalId);
        verify(events, times(1)).publishEvent(any(KrasnalHiddenEvent.class));
    }

    @Test
    void testDestroyKrasnal_removeKrasnalFromRepoAndPublishEvent() {
        int krasnalId = 29;
        Krasnal krasnal = Krasnal.newObject(krasnalId, "krasnal student", "krasnal student", new Position(32.4, 45.3));

        when(krasnalHandleService.GetKrasnal(krasnalId)).thenReturn(krasnal);
        when(repo.DestroyKrasnal(krasnalId)).thenReturn(true);

        boolean result = krasnalHandleService.DestroyKrasnal(krasnalId);

        assertTrue(result);
        verify(repo, times(1)).DestroyKrasnal(krasnalId);
        verify(events, times(1)).publishEvent(any(KrasnalDestroyedEvent.class));
    }
}
