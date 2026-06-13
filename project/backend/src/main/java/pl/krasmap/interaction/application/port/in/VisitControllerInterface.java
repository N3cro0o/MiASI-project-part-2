package pl.krasmap.interaction.application.port.in;

import pl.krasmap.interaction.application.domain.fav.NewVisit;
import pl.krasmap.interaction.application.domain.fav.VisitedKrasnal;

import java.util.List;

public interface VisitControllerInterface {
    VisitedKrasnal GetVisit(int visitedId);
    VisitedKrasnal GetVisit(int userId, int krasnalId);
    List<VisitedKrasnal> GetVisitsFromKrasnal(int krasnalId);
    List<VisitedKrasnal> GetVisitsFromUser(int userId);
    VisitedKrasnal AddVisit(NewVisit visit);
    Boolean RemoveVisit(int visitedId);
    Boolean RemoveVisit(int userId, int krasnalId);
}
