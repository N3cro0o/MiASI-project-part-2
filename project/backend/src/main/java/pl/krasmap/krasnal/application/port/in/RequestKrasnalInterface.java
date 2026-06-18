package pl.krasmap.krasnal.application.port.in;

import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;

public interface RequestKrasnalInterface {
    void AddNewKrasnal(String name, String description, Position position, KrasnalCategory category);
}
