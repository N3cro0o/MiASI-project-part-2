package pl.krasmap.krasnal.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.krasnal.application.domain.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;
import pl.krasmap.krasnal.application.port.out.KrasnalFetchInterface;
import pl.krasmap.krasnal.infrastructure.out.KrasnalFetchPostgres;

import java.util.List;

@Service
public class HoldKrasnalRepo {
    final private KrasnalFetchInterface krasnalFetch;

    public Krasnal ReturnDummyKrasnal() {
        return Krasnal.dummy();
    }

    public HoldKrasnalRepo (KrasnalFetchInterface fetch) {
        krasnalFetch = fetch;
    }

    public Krasnal GetKrasnal(int id) {
        return krasnalFetch.GetKrasnal(id);
    }

    public List<Krasnal> GetKrasnalList() {
        return krasnalFetch.GetAllKrasnalObjects();
    }

    public Krasnal AddNewKrasnal(KrasnalWeb newKrasnal) {
        Krasnal toAdd = Krasnal.from(newKrasnal);
        int id = krasnalFetch.SaveKrasnal(toAdd);
        return krasnalFetch.GetKrasnal(id);
    }

    public Krasnal UpdateKrasnal(int krasnalId, KrasnalWeb krasnalToUpdate) {
        Krasnal toAdd = Krasnal.from(krasnalId, krasnalToUpdate);
        krasnalFetch.UpdateKrasnal(toAdd);
        return krasnalFetch.GetKrasnal(krasnalId);
    }

    public boolean HideKrasnal(int krasnalId) {
        return krasnalFetch.HideKrasnal(krasnalId);
    }
}
