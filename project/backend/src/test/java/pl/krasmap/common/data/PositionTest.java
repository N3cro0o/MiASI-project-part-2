package pl.krasmap.common.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    void testPositionCreation_fieldsSetCorrectly() {
        double lat = 12.3;
        double lon = 45.6;

        Position position = new Position(lat, lon);

        assertEquals(lat, position.latitude());
        assertEquals(lon, position.longitude());
    }

    @Test
    void testPositionFrom_convertsFloatToDouble() {
        float lat = 12.3f;
        float lon = 45.6f;

        Position result = Position.from(lat, lon);

        assertEquals((double)lat, result.latitude(), 0.0001);
        assertEquals((double)lon, result.longitude(), 0.0001);
    }
}
