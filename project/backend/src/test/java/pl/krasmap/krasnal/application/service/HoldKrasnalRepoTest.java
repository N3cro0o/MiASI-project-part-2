package pl.krasmap.krasnal.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.UpdateTime;
import pl.krasmap.krasnal.application.domain.data.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.data.krasnal.Krasnal;
import pl.krasmap.krasnal.application.domain.data.krasnal.KrasnalStatus;
import pl.krasmap.krasnal.application.port.out.KrasnalFetchInterface;
import pl.krasmap.krasnal.application.service.HoldKrasnalRepo;


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
    void testAddNewKrasnal_callsSaveAndReturnsId() {
        Krasnal toAdd = Krasnal.newObject(1, "krasnal student", "krasnal student z piwem", Position.from(1.23f, 7.32f), KrasnalCategory.Dwarf, KrasnalStatus.Active, UpdateTime.now());
        int generatedId = 1;

        when(fetch.SaveKrasnal(toAdd)).thenReturn(generatedId);

        int result = repo.AddNewKrasnal(toAdd);

        assertEquals(generatedId, result);
        verify(fetch, times(1)).SaveKrasnal(toAdd);
    }

    @Test
    void testUpdateKrasnal_callsUpdateAndReturnsId() {
        Krasnal toAdd = Krasnal.newObject(1, "krasnal student", "krasnal student z piwem", Position.from(1.23f, 7.32f), KrasnalCategory.Dwarf, KrasnalStatus.Active, UpdateTime.now());
        int returnedId = 1;

        when(fetch.UpdateKrasnal(toAdd)).thenReturn(returnedId);

        int result = repo.UpdateKrasnal(toAdd);

        assertEquals(returnedId, result);
        verify(fetch, times(1)).UpdateKrasnal(toAdd);
    }
}
