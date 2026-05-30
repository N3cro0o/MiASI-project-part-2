package pl.krasmap.krasnal.infrastructure.in;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;
import pl.krasmap.krasnal.application.port.in.KrasnalControllerInterface;
import pl.krasmap.krasnal.application.service.HoldKrasnalRepo;

@RestController
@RequestMapping("/krasnal")
public class KrasnalControllerWeb implements KrasnalControllerInterface {

    AnnotationConfigApplicationContext krasnalContext;

    public KrasnalControllerWeb() {
        krasnalContext = new AnnotationConfigApplicationContext();
        krasnalContext.scan("pl.krasmap.krasnal");
    }

    @GetMapping("/{krasnalId}")
    @Override
    public Krasnal GetKrasnal(@PathVariable int krasnalId) {
        krasnalContext.refresh();
        HoldKrasnalRepo service = krasnalContext.getBean(HoldKrasnalRepo.class);
        return service.ReturnDummyKrasnal();
    }
}
