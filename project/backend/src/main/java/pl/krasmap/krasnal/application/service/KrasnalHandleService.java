package pl.krasmap.krasnal.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.krasmap.krasnal.application.domain.data.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.data.krasnal.Krasnal;
import pl.krasmap.krasnal.application.domain.event.KrasnalAddedEvent;
import pl.krasmap.krasnal.application.domain.event.KrasnalDestroyedEvent;
import pl.krasmap.krasnal.application.domain.event.KrasnalHiddenEvent;
import pl.krasmap.krasnal.application.domain.event.KrasnalUpdatedEvent;

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
        return GetKrasnal(id);
    }

    public Krasnal UpdateKrasnal(int krasnalId, KrasnalWeb krasnalToUpdate) {
        Krasnal toAdd = Krasnal.from(krasnalId, krasnalToUpdate);
        repos.UpdateKrasnal(toAdd);
        events.publishEvent(new KrasnalUpdatedEvent(krasnalId, toAdd.name(), toAdd.status()));
        return GetKrasnal(krasnalId);
    }

    public boolean HideKrasnal(int krasnalId) {
        Krasnal k = GetKrasnal(krasnalId);
        boolean b = repos.HideKrasnal(krasnalId);
        events.publishEvent(new KrasnalHiddenEvent(krasnalId, k.name()));
        return b;
    }

    public boolean DestroyKrasnal(int krasnalId) {
        Krasnal k = GetKrasnal(krasnalId);
        boolean b = repos.DestroyKrasnal(krasnalId);
        events.publishEvent(new KrasnalDestroyedEvent(krasnalId, k.name()));
        return b;
    }
}
