package pl.krasmap.interaction.application.port.in;

import pl.krasmap.interaction.application.domain.fav.VisitedKrasnal;

import java.util.List;

public interface RequestVisitedInterface {
    List<VisitedKrasnal> GetVisitedForUser(int userId);
}
