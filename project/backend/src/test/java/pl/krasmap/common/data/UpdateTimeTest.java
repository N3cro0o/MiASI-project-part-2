package pl.krasmap.common.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;

public class UpdateTimeTest {

    @Test
    void testUpdateTimeCreation_fieldsSetCorrectly() {
        OffsetDateTime created = OffsetDateTime.parse("2026-06-11T12:00:00+00:00");
        OffsetDateTime updated = OffsetDateTime.parse("2026-06-11T14:00:00+00:00");

        UpdateTime updateTime = new UpdateTime(created, updated);

        assertEquals(created, updateTime.created());
        assertEquals(updated, updateTime.updated());
    }

    @Test
    void testUpdateTimeNow_createdAndUpdatedCloseToNow() {
        OffsetDateTime before = OffsetDateTime.now();
        UpdateTime updateTime = UpdateTime.now();
        OffsetDateTime after = OffsetDateTime.now();

        assertEquals(updateTime.created(), updateTime.updated(), "Created and updated must be equal");
        assertFalse(updateTime.created().isAfter(after), "Created and updated must be sooner than after");
        assertFalse(updateTime.created().isBefore(before), "Created and updated must be later than before");
    }

    @Test
    void testUpdateTimeFrom_createsFromProvidedValues() {
        OffsetDateTime created = OffsetDateTime.parse("2026-06-11T12:00:00+00:00");
        OffsetDateTime updated = OffsetDateTime.parse("2026-06-11T14:00:00+00:00");

        UpdateTime result = UpdateTime.from(created, updated);

        assertEquals(created, result.created());
        assertEquals(updated, result.updated());
    }
}
