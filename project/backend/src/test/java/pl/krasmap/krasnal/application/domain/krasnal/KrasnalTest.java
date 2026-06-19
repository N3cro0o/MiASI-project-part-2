package pl.krasmap.krasnal.application.domain.krasnal;

import org.junit.jupiter.api.Test;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.UpdateTime;
import pl.krasmap.krasnal.application.domain.data.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.data.krasnal.Krasnal;
import pl.krasmap.krasnal.application.domain.data.krasnal.KrasnalStatus;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class KrasnalTest {

    @Test
    void testDummy_returnsDummyKrasnal() {
        Krasnal dummy = Krasnal.dummy();

        assertNotNull(dummy);
        assertEquals(-1, dummy.id());
        assertEquals("Lorem ipsum", dummy.name());
    }

    @Test
    void testNewObject_noCategory_noUpdate_noStatus_returnsNewKrasnalWithDefaults() {
        Krasnal krasnal = Krasnal.newObject(2, "krasnal student", "krasnal student z piwem", new Position(3.22, 53.23));

        assertNotNull(krasnal);
        assertEquals(KrasnalCategory.Dwarf, krasnal.category());
        assertEquals(KrasnalStatus.Inactive, krasnal.status());

        assertTrue(krasnal.time().created().isBefore(OffsetDateTime.now().plusMinutes(1)));
        assertTrue(krasnal.time().updated().isBefore(OffsetDateTime.now().plusMinutes(1)));
    }

    @Test
    void testNewObject_returnsNewKrasnal() {
        Krasnal krasnal = Krasnal.newObject(2,
                "krasnal student",
                "krasnal student z piwem",
                new Position(3.22, 53.23),
                KrasnalCategory.Building,
                KrasnalStatus.Archived,
                new UpdateTime(OffsetDateTime.now().minusDays(1), OffsetDateTime.now().plusDays(1)));

        assertNotNull(krasnal);
        assertEquals("krasnal student", krasnal.name());
        assertEquals(KrasnalCategory.Building, krasnal.category());
        assertEquals(KrasnalStatus.Archived, krasnal.status());
        assertTrue(krasnal.time().created().isBefore(OffsetDateTime.now()));
        assertTrue(krasnal.time().updated().isAfter(OffsetDateTime.now()));
    }

    @Test
    void testFrom_returnsNewKrasnal() {
        KrasnalWeb krasnal = new KrasnalWeb("krasnal student", "krasnal student z piwem", new Position(32.45, 65.33), KrasnalCategory.Dwarf, KrasnalStatus.Active, 0.0);

        Krasnal newKrasnal = Krasnal.from(3, krasnal);
        assertNotNull(newKrasnal);
        assertEquals("krasnal student", newKrasnal.name());
        assertEquals(3, newKrasnal.id());
    }

    @Test
    void testFrom_noId_returnsNewKrasnalWithSetId() {
        KrasnalWeb krasnal = new KrasnalWeb("krasnal student", "krasnal student z piwem", new Position(32.45, 65.33), KrasnalCategory.Dwarf, KrasnalStatus.Active, 0.0);

        Krasnal newKrasnal = Krasnal.from(krasnal);
        assertNotNull(newKrasnal);
        assertEquals("krasnal student", newKrasnal.name());
        assertEquals(-1, newKrasnal.id());
    }

    @Test
    void testToString_returnsCorrectFormat() {
        Krasnal krasnal = Krasnal.newObject(2,
                "krasnal student",
                "krasnal student z piwem",
                new Position(3.22, 53.23),
                KrasnalCategory.Building,
                KrasnalStatus.Archived,
                new UpdateTime(OffsetDateTime.now(), OffsetDateTime.now().plusDays(1)));

        assertEquals("Krasnal(2) krasnal student", krasnal.toString());
    }
}
