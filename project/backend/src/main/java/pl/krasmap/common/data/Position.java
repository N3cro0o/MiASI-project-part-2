package pl.krasmap.common.data;

public record Position(double latitude, double longitude) {

    public static Position from(float latitude, float longitude) {
        return new Position(latitude, longitude);
    }
}
