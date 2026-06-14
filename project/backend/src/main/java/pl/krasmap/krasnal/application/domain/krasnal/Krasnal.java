package pl.krasmap.krasnal.application.domain.krasnal;

import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.UpdateTime;
import pl.krasmap.krasnal.application.domain.KrasnalWeb;

public record Krasnal(int id, String name, String description, Position position, KrasnalCategory category,
                      KrasnalStatus status, UpdateTime time, Double averageRating) {

    public static Krasnal dummy() {
        return new Krasnal(-1, "Lorem ipsum", "Lorem ipsum dolor sit amet", new Position(51.10, 17.03), KrasnalCategory.Place, KrasnalStatus.Inactive, UpdateTime.now(), 0.0);
    }

    public static Krasnal newObject(int id, String name, String desc, Position pos) {
        return new Krasnal(id, name, desc, pos, KrasnalCategory.Dwarf, KrasnalStatus.Inactive, UpdateTime.now(), 0.0);
    }

    public static Krasnal newObject(int id, String name, String desc, Position pos, KrasnalCategory cat, KrasnalStatus stat, UpdateTime time) {
        return new Krasnal(id, name, desc, pos, cat, stat, time, 0.0);
    }

    public static Krasnal newObject(int id, String name, String desc, Position pos, UpdateTime time) {
        return new Krasnal(id, name, desc, pos, KrasnalCategory.Dwarf, KrasnalStatus.Inactive, time, 0.0);
    }

    public static Krasnal newObject(int id, String name, String desc, Position pos, KrasnalCategory cat, KrasnalStatus stat) {
        return new Krasnal(id, name, desc, pos, cat, stat, UpdateTime.now(), 0.0);
    }

    public static Krasnal newObject(int id, String name, String desc, Position pos, KrasnalCategory cat, KrasnalStatus stat, UpdateTime time, Double averageRating) {
        return new Krasnal(id, name, desc, pos, cat, stat, time, averageRating);
    }

    public static Krasnal from(KrasnalWeb krasnal) {
        return Krasnal.newObject(-1, krasnal.name(), krasnal.description(), krasnal.position(), krasnal.category(), krasnal.status(), UpdateTime.now(), krasnal.averageRating());
    }

    public static Krasnal from(int id, KrasnalWeb krasnal) {
        return Krasnal.newObject(id, krasnal.name(), krasnal.description(), krasnal.position(), krasnal.category(), krasnal.status(), UpdateTime.now(), krasnal.averageRating());
    }

    @Override
    public String toString() {
        return String.format("Krasnal(%d) %s", this.id, this.name);
    }
}
