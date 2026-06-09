package pl.krasmap.krasnal.infrastructure.in;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;
import pl.krasmap.krasnal.application.port.in.KrasnalControllerInterface;
import pl.krasmap.krasnal.application.service.HoldKrasnalRepo;

import java.util.List;

@RestController
@RequestMapping("/api/krasnal")
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

    @Override
    @PostMapping("/new")
    @RequestBody
    public Krasnal SaveNewKrasnal(Krasnal newKrasnal) {
        return null;
    }

    @Override
    public boolean DeleteKrasnal(int krasnalID) {
        return false;
    }

    @Override
    public Krasnal UpdateKrasnal(Krasnal krasnalToUpdate) {
        return null;
    }
}
