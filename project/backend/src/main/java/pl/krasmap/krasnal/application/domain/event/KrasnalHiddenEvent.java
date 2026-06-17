package pl.krasmap.krasnal.application.domain.event;

import pl.krasmap.krasnal.application.domain.data.krasnal.KrasnalStatus;

public record KrasnalHiddenEvent(int krasnalId, String name) {
}
