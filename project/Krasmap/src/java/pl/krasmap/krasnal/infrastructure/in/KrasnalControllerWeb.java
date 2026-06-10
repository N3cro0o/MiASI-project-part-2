package pl.krasmap.krasnal.infrastructure.in;

import org.springframework.web.bind.annotation.*;
import pl.krasmap.krasnal.application.domain.KrasnalWeb;
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

    @GetMapping("/get/{krasnalId}")
    @Override
    public Krasnal GetKrasnal(@PathVariable int krasnalId) {
        return krasnalRepo.GetKrasnal(krasnalId);
    }

    @GetMapping("/get/all")
    @Override
    public List<Krasnal> GetAllKrasnal() {
        return krasnalRepo.GetKrasnalList();
    }

    @Override
    @PostMapping("/new")
    public Krasnal SaveNewKrasnal(@RequestBody KrasnalWeb newKrasnal) {
        System.out.println(newKrasnal);
        return krasnalRepo.AddNewKrasnal(newKrasnal);
    }

    @Override
    @DeleteMapping("/delete/{krasnalId}")
    public boolean DeleteKrasnal(@PathVariable int krasnalId) {
        return krasnalRepo.HideKrasnal(krasnalId);
    }

    @Override
    @PatchMapping("/update/{krasnalId}")
    public Krasnal UpdateKrasnal(@RequestBody KrasnalWeb krasnalToUpdate, @PathVariable int krasnalId) {
        System.out.println(krasnalToUpdate);
        return krasnalRepo.UpdateKrasnal(krasnalId, krasnalToUpdate);
    }
}
