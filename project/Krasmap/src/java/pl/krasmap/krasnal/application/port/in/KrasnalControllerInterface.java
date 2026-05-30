package pl.krasmap.krasnal.application.port.in;

import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;

public interface KrasnalControllerInterface {
    Krasnal GetKrasnal(int krasnalId);
}
