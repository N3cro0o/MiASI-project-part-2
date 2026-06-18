package pl.krasmap.krasnal.infrastructure.in;

import org.springframework.stereotype.Component;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.krasnal.application.domain.data.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.data.krasnal.KrasnalStatus;
import pl.krasmap.krasnal.application.port.in.RequestKrasnalInterface;
import pl.krasmap.krasnal.application.service.KrasnalHandleService;

@Component
public class RequestKrasnalFromContext implements RequestKrasnalInterface {

    private final KrasnalHandleService krasnalHandle;

    public RequestKrasnalFromContext(KrasnalHandleService handle){
        krasnalHandle = handle;
    }

    @Override
    public void AddNewKrasnal(String name, String description, Position position, KrasnalCategory category) {
        KrasnalWeb k = new KrasnalWeb(name, description, position, category, KrasnalStatus.Active, 0.0);
        krasnalHandle.AddNewKrasnal(k);
    }
}
