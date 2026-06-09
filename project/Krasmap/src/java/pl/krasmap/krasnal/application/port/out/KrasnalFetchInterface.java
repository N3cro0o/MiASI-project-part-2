package pl.krasmap.krasnal.application.port.out;

import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;

import java.util.List;

public interface KrasnalFetchInterface {
    List<Krasnal> GetAllKrasnalObjects();
    void SaveOrUpdateKrasnal(Krasnal k);
}
