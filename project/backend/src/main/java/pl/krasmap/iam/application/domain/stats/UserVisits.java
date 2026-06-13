package pl.krasmap.iam.application.domain.stats;

import pl.krasmap.interaction.application.domain.fav.VisitedKrasnal;

import java.time.OffsetDateTime;

public record UserVisits(int krasnalId, OffsetDateTime time) {

    public static UserVisits From(VisitedKrasnal k) {
        return new UserVisits(k.krasnalId(), k.time());
    }
}
