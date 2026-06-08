package pl.krasmap.krasnal.application.domain.krasnal;

public record Krasnal(int id, String name, String description, Position position, KrasnalCategory category,
                      KrasnalStatus status, UpdateTime time) {

    public static Krasnal dummy() {
        return new Krasnal(-1, "Lorem ipsum", "Lorem ipsum dolor sit amet", new Position(51.10, 17.03), KrasnalCategory.Place, KrasnalStatus.Inactive, UpdateTime.now());
    }

    public static Krasnal newObject(int id, String name, String desc, Position pos) {
        return new Krasnal(id, name, desc, pos, KrasnalCategory.Dwarf, KrasnalStatus.Inactive, UpdateTime.now());
    }

    public static Krasnal newObject(int id, String name, String desc, Position pos, KrasnalCategory cat, KrasnalStatus stat, UpdateTime time) {
        return new Krasnal(id, name, desc, pos, cat, stat, time);
    }

    public static Krasnal newObject(int id, String name, String desc, Position pos, UpdateTime time) {
        return new Krasnal(id, name, desc, pos, KrasnalCategory.Dwarf, KrasnalStatus.Inactive, time);
    }

    @Override
    public String toString() {
        return String.format("Krasnal(%d) %s", this.id, this.name);
    }
}
