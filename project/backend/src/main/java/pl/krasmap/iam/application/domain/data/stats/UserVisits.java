package pl.krasmap.iam.application.domain.data.stats;

import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;

import java.time.OffsetDateTime;

public record UserVisits(int krasnalId, OffsetDateTime time) {

    public static UserVisits From(VisitedKrasnal k) {
        return new UserVisits(k.krasnalId(), k.time());
    }
}
