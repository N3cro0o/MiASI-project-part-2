package pl.krasmap.krasnal.application.port.in;

import pl.krasmap.krasnal.application.domain.KrasnalReview;
import pl.krasmap.krasnal.application.domain.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;

import java.util.List;

public interface KrasnalControllerInterface {
    Krasnal GetKrasnal(int krasnalId);
    List<KrasnalReview> GetKrasnalReviews(int krasnalId);
    List<Krasnal> GetAllKrasnal();
    Krasnal SaveNewKrasnal(KrasnalWeb newKrasnal);
    Boolean DeleteKrasnal(int krasnalID);
    Krasnal UpdateKrasnal(KrasnalWeb krasnalToUpdate, int krasnalId);
}
