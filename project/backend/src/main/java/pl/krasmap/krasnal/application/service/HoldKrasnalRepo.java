package pl.krasmap.krasnal.application.service;

import org.springframework.stereotype.Repository;
import pl.krasmap.krasnal.application.domain.data.krasnal.Krasnal;
import pl.krasmap.krasnal.application.port.out.KrasnalFetchInterface;

import java.util.List;

@Repository
public class HoldKrasnalRepo {
    final private KrasnalFetchInterface krasnalFetch;

    public HoldKrasnalRepo (KrasnalFetchInterface fetch) {
        krasnalFetch = fetch;
    }

    public Krasnal GetKrasnal(int id) {
        return krasnalFetch.GetKrasnal(id);
    }

    public List<Krasnal> GetKrasnalList() {
        return krasnalFetch.GetAllKrasnalObjects();
    }

    public int AddNewKrasnal(Krasnal toAdd) {
        return krasnalFetch.SaveKrasnal(toAdd);
    }

    public int UpdateKrasnal(Krasnal toAdd) {
        return krasnalFetch.UpdateKrasnal(toAdd);
    }

    public boolean HideKrasnal(int krasnalId) {
        return krasnalFetch.HideKrasnal(krasnalId);
    }

    public boolean DestroyKrasnal(int krasnalId) {
        return krasnalFetch.DeleteKrasnal(krasnalId);
    }
}
