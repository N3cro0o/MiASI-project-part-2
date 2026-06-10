package pl.krasmap.common.data;

public enum KrasnalCategory {
    Monument,
    Building,
    Dwarf,
    Flora,
    Place;

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
