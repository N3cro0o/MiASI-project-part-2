package pl.krasmap.interaction.application.port.out;

import pl.krasmap.interaction.application.domain.data.fav.NewVisit;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;

import java.util.List;

public interface VisitedFetchInterface {
    VisitedKrasnal GetVisit(int visitedId);
    VisitedKrasnal GetVisit(int userId, int krasnalId);
    List<VisitedKrasnal> GetVisitsFromKrasnal(int krasnalId);
    List<VisitedKrasnal> GetVisitsFromUser(int userId);
    int AddVisit(NewVisit visit);
    boolean RemoveVisit(int visitedId);
    boolean RemoveVisit(int userId, int krasnalId);
}
