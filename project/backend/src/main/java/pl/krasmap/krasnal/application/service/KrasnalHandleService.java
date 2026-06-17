package pl.krasmap.krasnal.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.krasmap.krasnal.application.domain.data.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.data.krasnal.Krasnal;
import pl.krasmap.krasnal.application.domain.event.KrasnalAddedEvent;

import java.util.List;

@Service
public class KrasnalHandleService {

    private final HoldKrasnalRepo repos;
    private final ApplicationEventPublisher events;

    public KrasnalHandleService(HoldKrasnalRepo r, ApplicationEventPublisher e){
        events = e;
        repos = r;
    }

    public Krasnal GetKrasnal(int id) {
        return repos.GetKrasnal(id);
    }

    public List<Krasnal> GetKrasnalList() {
        return repos.GetKrasnalList();
    }

    public Krasnal AddNewKrasnal(KrasnalWeb newKrasnal) {
        Krasnal toAdd = Krasnal.from(newKrasnal);
        int id = repos.AddNewKrasnal(toAdd);
        events.publishEvent(new KrasnalAddedEvent(id, toAdd.name(), toAdd.status()));
        return repos.GetKrasnal(id);
    }

    public Krasnal UpdateKrasnal(int krasnalId, KrasnalWeb krasnalToUpdate) {
        Krasnal toAdd = Krasnal.from(krasnalId, krasnalToUpdate);
        repos.UpdateKrasnal(toAdd);
        return repos.GetKrasnal(krasnalId);
    }

    public boolean HideKrasnal(int krasnalId) {
        return repos.HideKrasnal(krasnalId);
    }
}
