package pl.krasmap.krasnal.application.domain.krasnal;

public enum KrasnalStatus {
    Active,
    Inactive,
    Archived;

    public static KrasnalStatus FromString(String stat) {
        return switch (stat.toLowerCase()) {
            case "active" -> KrasnalStatus.Active;
            case "inactive", "non active" -> KrasnalStatus.Inactive;
            default -> KrasnalStatus.Archived;
        };
    }
}

