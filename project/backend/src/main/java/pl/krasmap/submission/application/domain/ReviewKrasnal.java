package pl.krasmap.submission.application.domain;

import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;

public record ReviewKrasnal(String name, String description, Position position, KrasnalCategory category) {
}
