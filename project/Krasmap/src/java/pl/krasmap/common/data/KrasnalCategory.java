package pl.krasmap.common.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public enum KrasnalCategory {
    Monument,
    Building,
    Dwarf,
    Flora,
    Place;

    @JsonCreator
    public static KrasnalCategory FromString(String cat) {
        return switch (cat.toLowerCase()) {
            case "monument" -> KrasnalCategory.Monument;
            case "building" -> KrasnalCategory.Building;
            case "flora" -> KrasnalCategory.Flora;
            case "place" -> KrasnalCategory.Place;
            default -> KrasnalCategory.Dwarf;
        };
    }


    @Override
    @JsonValue
    public String toString() {
        return switch (this) {
            case Monument -> "MONUMENT";
            case Building -> "BUILDING";
            case Dwarf -> "KRASNAL_FIGURINE";
            case Flora -> "FLORA";
            case Place -> "PLACE";
        };
    }
}
