package pl.krasmap.interaction.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.krasmap.interaction.application.domain.data.fav.NewVisit;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;
import pl.krasmap.interaction.application.domain.event.fav.VisitedAddedEvent;
import pl.krasmap.interaction.application.domain.event.fav.VisitedDeletedEvent;

import java.util.List;

@Service
public class HandleVisitedService {

    private final HoldVisitedRepo repos;
    private final ApplicationEventPublisher events;

    public HandleVisitedService(HoldVisitedRepo r, ApplicationEventPublisher e){
        repos = r;
        events = e;
    }

    public VisitedKrasnal GetVisit(int visitedId){
        return repos.GetVisit(visitedId);
    }

    public VisitedKrasnal GetVisit(int userId, int krasnalId){
        return repos.GetVisit(userId, krasnalId);
    }

    public List<VisitedKrasnal> GetVisitsFromKrasnal(int krasnalId){
        return repos.GetVisitsFromKrasnal(krasnalId);
    }

    public List<VisitedKrasnal> GetVisitsFromUser(int userId){
        return repos.GetVisitsFromUser(userId);
    }

    public VisitedKrasnal AddVisit(NewVisit visit){
        int id = repos.AddVisit(visit);
        events.publishEvent(new VisitedAddedEvent(id, visit.krasnalId(), visit.userId()));
        return GetVisit(id);
    }

    public Boolean RemoveVisit(int visitedId){
        VisitedKrasnal v = GetVisit(visitedId);
        boolean b = repos.RemoveVisit(visitedId);
        events.publishEvent(new VisitedDeletedEvent(visitedId, v.krasnalId(), v.userId()));
        return b;
    }

    public Boolean RemoveVisit(int userId, int krasnalId) {
        VisitedKrasnal v = GetVisit(userId, krasnalId);
        boolean b = repos.RemoveVisit(userId, krasnalId);
        events.publishEvent(new VisitedDeletedEvent(v.id(), krasnalId, userId));
        return b;
    }
}
