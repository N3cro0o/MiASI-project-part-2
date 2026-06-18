package pl.krasmap.interaction.infrastructure.in;

import org.springframework.stereotype.Component;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;
import pl.krasmap.interaction.application.port.in.RequestVisitedInterface;
import pl.krasmap.interaction.application.service.HandleVisitedService;

import java.util.List;

@Component
public class RequestVisitedFromContext implements RequestVisitedInterface {

    private final HandleVisitedService visitHandle;

    public RequestVisitedFromContext(HandleVisitedService repo) {
        visitHandle = repo;
    }

    @Override
    public List<VisitedKrasnal> GetVisitedForUser(int userId) {
        return visitHandle.GetVisitsFromUser(userId);
    }
}
