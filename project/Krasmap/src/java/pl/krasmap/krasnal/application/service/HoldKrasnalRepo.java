package pl.krasmap.krasnal.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;
import pl.krasmap.krasnal.application.port.out.KrasnalFetchInterface;
import pl.krasmap.krasnal.infrastructure.out.KrasnalFetchPostgres;

import java.util.List;

@Service
public class HoldKrasnalRepo {
    final private List<Krasnal> krasnalList;

    public Krasnal ReturnDummyKrasnal() {
        return Krasnal.dummy();
    }

    public HoldKrasnalRepo () {
        KrasnalFetchInterface dbConn = new KrasnalFetchPostgres();
        krasnalList = dbConn.GetAllKrasnalObjects();
    }

    public Krasnal GetKrasnal(int id) {
        Krasnal obj = null;
        for (Krasnal k : krasnalList) {
            if (k.id() == id) { obj = k; break; }
        }
        return obj;
    }

    public List<Krasnal> GetKrasnalList() {
        return krasnalList;
    }
}
