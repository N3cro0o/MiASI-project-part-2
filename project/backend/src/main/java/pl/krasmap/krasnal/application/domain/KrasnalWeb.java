package pl.krasmap.krasnal.application.domain;

import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.krasnal.application.domain.krasnal.KrasnalStatus;
import pl.krasmap.common.data.Position;

public record KrasnalWeb(String name, String description, Position position, KrasnalCategory category, KrasnalStatus status, Double averageRating) {
}
