package pl.krasmap.krasnal.application.domain.krasnal;

public enum KrasnalStatus {
    Active,
    Inactive,
    Archived;

    public static KrasnalStatus FromString(String stat) {
        return switch (stat.toLowerCase()) {
            case "active" -> KrasnalStatus.Active;
            case "inactive" -> KrasnalStatus.Inactive;
            default -> KrasnalStatus.Archived;
        };
    }


    @Override
    public String toString() {
        return switch (this) {
            case Active -> "ACTIVE";
            case Inactive -> "INACTIVE";
            case Archived -> "ARCHIVED";
        };
    }
}

