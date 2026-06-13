package pl.krasmap.interaction.infrastructure.in;

import org.springframework.stereotype.Component;
import pl.krasmap.interaction.application.domain.fav.VisitedKrasnal;
import pl.krasmap.interaction.application.port.in.RequestVisitedInterface;
import pl.krasmap.interaction.application.service.HoldVisitedRepo;

import java.util.List;

@Component
public class RequestVisitedFromContext implements RequestVisitedInterface {

    private final HoldVisitedRepo visitRepo;

    public RequestVisitedFromContext(HoldVisitedRepo repo) {
        visitRepo = repo;
    }

    @Override
    public List<VisitedKrasnal> GetVisitedForUser(int userId) {
        return visitRepo.GetVisitsFromUser(userId);
    }
}
