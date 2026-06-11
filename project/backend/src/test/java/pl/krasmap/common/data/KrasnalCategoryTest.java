package pl.krasmap.common.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KrasnalCategoryTest {

    @Test
    void testFromString_knownCategories_shouldReturnProperEnum() {
        assertEquals(KrasnalCategory.Monument, KrasnalCategory.FromString("monument"));
        assertEquals(KrasnalCategory.Place, KrasnalCategory.FromString("Place"));
        assertEquals(KrasnalCategory.Flora, KrasnalCategory.FromString("FLORA"));
        assertEquals(KrasnalCategory.Building, KrasnalCategory.FromString("BuiLdiNg"));
    }

    @Test
    void testFromString_unknownCategory_shouldReturnDwarf() {
        assertEquals(KrasnalCategory.Dwarf, KrasnalCategory.FromString("cokolwiek"));
        assertEquals(KrasnalCategory.Dwarf, KrasnalCategory.FromString(""));
    }

    @Test
    void testToString_shouldReturnExpectedFormat() {
        assertEquals("MONUMENT", KrasnalCategory.Monument.toString());
        assertEquals("PLACE", KrasnalCategory.Place.toString());
        assertEquals("FLORA", KrasnalCategory.Flora.toString());
        assertEquals("BUILDING", KrasnalCategory.Building.toString());
        assertEquals("KRASNAL_FIGURINE", KrasnalCategory.Dwarf.toString());
    }
}
