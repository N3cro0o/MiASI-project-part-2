package pl.krasmap.krasnal.infrastructure.in;

import org.springframework.stereotype.Component;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.krasnal.application.domain.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.krasnal.KrasnalStatus;
import pl.krasmap.krasnal.application.port.in.RequestKrasnalInterface;
import pl.krasmap.krasnal.application.service.HoldKrasnalRepo;

@Component
public class RequestKrasnalFromContext implements RequestKrasnalInterface {

    private final HoldKrasnalRepo krasnalRepo;

    public RequestKrasnalFromContext(HoldKrasnalRepo repo){
        krasnalRepo = repo;
    }

    @Override
    public void AddNewKrasnal(String name, String description, Position position, KrasnalCategory category) {
        KrasnalWeb k = new KrasnalWeb(name, description, position, category, KrasnalStatus.Active, 0.0);
        krasnalRepo.AddNewKrasnal(k);
    }
}
