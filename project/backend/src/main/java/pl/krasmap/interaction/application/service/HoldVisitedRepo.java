package pl.krasmap.interaction.application.service;

import org.springframework.stereotype.Repository;
import pl.krasmap.interaction.application.domain.data.fav.NewVisit;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;
import pl.krasmap.interaction.application.port.out.VisitedFetchInterface;

import java.util.List;

@Repository
public class HoldVisitedRepo {

    private final VisitedFetchInterface visitFetch;

    public HoldVisitedRepo(VisitedFetchInterface fetch){
        visitFetch = fetch;
    }

    public VisitedKrasnal GetVisit(int visitedId){
        return visitFetch.GetVisit(visitedId);
    }

    public VisitedKrasnal GetVisit(int userId, int krasnalId){
        return visitFetch.GetVisit(userId, krasnalId);
    }

    public List<VisitedKrasnal> GetVisitsFromKrasnal(int krasnalId){
        return visitFetch.GetVisitsFromKrasnal(krasnalId);
    }

    public List<VisitedKrasnal> GetVisitsFromUser(int userId){
        return visitFetch.GetVisitsFromUser(userId);
    }

    public int AddVisit(NewVisit visit){
        return visitFetch.AddVisit(visit);
    }

    public Boolean RemoveVisit(int visitedId){
        return visitFetch.RemoveVisit(visitedId);
    }

    public Boolean RemoveVisit(int userId, int krasnalId) {
        return visitFetch.RemoveVisit(userId, krasnalId);
    }
}
