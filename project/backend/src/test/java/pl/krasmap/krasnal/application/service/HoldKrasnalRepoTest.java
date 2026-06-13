package pl.krasmap.krasnal.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.krasnal.application.domain.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.krasnal.KrasnalStatus;
import pl.krasmap.krasnal.application.port.out.KrasnalFetchInterface;
import pl.krasmap.krasnal.application.service.HoldKrasnalRepo;
import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HoldKrasnalRepoTest {

    @Mock
    private KrasnalFetchInterface fetch;

    @InjectMocks
    private HoldKrasnalRepo repo;

    @Test
    void testReturnDummyKrasnal_returnsDummy() {
        Krasnal dummy = Krasnal.dummy();

        Krasnal result = repo.ReturnDummyKrasnal();

        assertNotNull(result);
        assertEquals(dummy, result);
    }

    @Test
    void testGetKrasnal_callsInterfaceAndReturnsKrasnal() {
        int krasnalId = 1;
        Krasnal returnedKrasnal = Krasnal.newObject(krasnalId, "krasnal student", "krasnal student z piwem", Position.from(1.32f, 4.23f));

        when(fetch.GetKrasnal(krasnalId)).thenReturn(returnedKrasnal);

        Krasnal result = repo.GetKrasnal(krasnalId);

        assertNotNull(result);
        assertEquals(returnedKrasnal, result);
        verify(fetch, times(1)).GetKrasnal(krasnalId);
    }

    @Test
    void testGetKrasnalList_callsInterfaceAndReturnsKrasnalList() {
        Krasnal k1 = Krasnal.newObject(1, "krasnal student", "krasnal student z piwem", Position.from(1.32f, 4.23f));
        Krasnal k2 = Krasnal.newObject(2, "stare drzewo", "bardzo stare drzewo", Position.from(13.32f, 6.12f), KrasnalCategory.Flora, KrasnalStatus.Active);

        when(fetch.GetAllKrasnalObjects()).thenReturn(List.of(k1, k2));

        List<Krasnal> result = repo.GetKrasnalList();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(k1));
        assertTrue(result.contains(k2));
        verify(fetch, times(1)).GetAllKrasnalObjects();
    }

    @Test
    void testAddNewKrasnal_callsSaveAndGet() {
        KrasnalWeb krasnal = new KrasnalWeb("krasnal student", "krasnal student z piwem", Position.from(1.23f, 7.32f), KrasnalCategory.Dwarf, KrasnalStatus.Active);
        int generatedId = 1;

        Krasnal toSave = Krasnal.from(krasnal);
        Krasnal expectedKrasnal = Krasnal.from(generatedId, krasnal);

        when(fetch.SaveKrasnal(toSave)).thenReturn(generatedId);
        when(fetch.GetKrasnal(generatedId)).thenReturn(expectedKrasnal);

        Krasnal result = repo.AddNewKrasnal(krasnal);

        assertNotNull(result);
        assertEquals(expectedKrasnal, result);
        assertEquals(generatedId, result.id());
        verify(fetch, times(1)).SaveKrasnal(toSave);
        verify(fetch, times(1)).GetKrasnal(generatedId);
    }

    @Test
    void testUpdateKrasnal_callsUpdateAndGet() {
        int krasnalId = 1;
        KrasnalWeb krasnal = new KrasnalWeb("krasnal student", "krasnal student z piwem", Position.from(1.23f, 7.32f), KrasnalCategory.Dwarf, KrasnalStatus.Active);
        Krasnal expectedKrasnal = Krasnal.from(krasnalId, krasnal);

        when(fetch.GetKrasnal(krasnalId)).thenReturn(expectedKrasnal);

        Krasnal result = repo.UpdateKrasnal(krasnalId, krasnal);

        assertNotNull(result);
        assertEquals(expectedKrasnal, result);
        assertEquals(krasnalId, result.id());
        verify(fetch, times(1)).UpdateKrasnal(expectedKrasnal);
        verify(fetch, times(1)).GetKrasnal(krasnalId);
    }
}
