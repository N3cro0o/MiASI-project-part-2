package pl.krasmap.krasnal.application.port.out;

import pl.krasmap.krasnal.application.domain.data.krasnal.Krasnal;

import java.util.List;

public interface KrasnalFetchInterface {
    List<Krasnal> GetAllKrasnalObjects();
    Krasnal GetKrasnal(int krasnalId);
    int SaveKrasnal(Krasnal k);
    int UpdateKrasnal(Krasnal k);
    boolean HideKrasnal(int krasnalId);
    boolean DeleteKrasnal(int krasnalId);
}
