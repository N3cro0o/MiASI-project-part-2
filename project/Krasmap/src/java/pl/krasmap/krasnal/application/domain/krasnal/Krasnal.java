package pl.krasmap.krasnal.application.domain.krasnal;

import pl.krasmap.common.Position;

public record Krasnal(int id, String name, String description, Position position, KrasnalCategory category,
                      KrasnalStatus status) {

    public static Krasnal dummy() {
        return new Krasnal(-1, "Lorem ipsum", "Lorem ipsum dolor sit amet", new Position(51.10, 17.03), KrasnalCategory.Place, KrasnalStatus.Inactive);
    }

    @Override
    public String toString() {
        return String.format("Krasnal(%d) %s", this.id, this.name);
    }
}
