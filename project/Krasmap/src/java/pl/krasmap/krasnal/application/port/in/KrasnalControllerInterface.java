package pl.krasmap.krasnal.application.port.in;

import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;

import java.util.List;

public interface KrasnalControllerInterface {
    Krasnal GetKrasnal(int krasnalId);
    List<Krasnal> GetAllKrasnal();
}
