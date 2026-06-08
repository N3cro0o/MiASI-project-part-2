package pl.krasmap.krasnal.infrastructure.in;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;
import pl.krasmap.krasnal.application.port.in.KrasnalControllerInterface;
import pl.krasmap.krasnal.application.service.HoldKrasnalRepo;

import java.util.List;

@RestController
@RequestMapping("/krasnal")
public class KrasnalControllerWeb implements KrasnalControllerInterface {

    private final HoldKrasnalRepo krasnalRepo;

    KrasnalControllerWeb(HoldKrasnalRepo repo) {
        krasnalRepo = repo;
    }

    // TODO
    // Change methods to handle HTTP Error codes -> add another method and exception handling

    @GetMapping("/{krasnalId}")
    @Override
    public Krasnal GetKrasnal(@PathVariable int krasnalId) {
        return krasnalRepo.GetKrasnal(krasnalId);
    }

    @GetMapping("/all")
    @Override
    public List<Krasnal> GetAllKrasnal() {
        return krasnalRepo.GetKrasnalList();
    }
}
