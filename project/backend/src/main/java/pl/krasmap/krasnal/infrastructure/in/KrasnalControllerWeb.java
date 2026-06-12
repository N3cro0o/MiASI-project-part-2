package pl.krasmap.krasnal.infrastructure.in;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.krasnal.application.domain.KrasnalReview;
import pl.krasmap.krasnal.application.domain.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;
import pl.krasmap.krasnal.application.port.in.KrasnalControllerInterface;
import pl.krasmap.krasnal.application.port.out.GetKrasnalReviewInterface;
import pl.krasmap.krasnal.application.service.HoldKrasnalRepo;

import java.util.List;

@RestController
@RequestMapping("/api/krasnal")
public class KrasnalControllerWeb implements KrasnalControllerInterface {

    private final HoldKrasnalRepo krasnalRepo;
    private final GetKrasnalReviewInterface krasnalReviews;
    private final UserAuthInterface auth;

    KrasnalControllerWeb(HoldKrasnalRepo repo, GetKrasnalReviewInterface review, @Lazy UserAuthInterface authServ) {
        krasnalReviews = review;
        krasnalRepo = repo;
        auth = authServ;
    }

    @GetMapping("/get/{krasnalId}")
    public ResponseEntity<Krasnal> GetKrasnalWrapper(@PathVariable int krasnalId) {
//        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
//        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
//        if (o) {
        Krasnal p = GetKrasnal(krasnalId);
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
//        }
//        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Krasnal GetKrasnal(int krasnalId) {
        return krasnalRepo.GetKrasnal(krasnalId);
    }

    @GetMapping("/get/{krasnalId}/review")
    public ResponseEntity<List<KrasnalReview>> GetKrasnalReviewsWrapper(@PathVariable int krasnalId) {
        List<KrasnalReview> p = GetKrasnalReviews(krasnalId);
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public List<KrasnalReview> GetKrasnalReviews(int krasnalId) {
        return krasnalReviews.GetAllReviews(krasnalId);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Krasnal>> GetAllKrasnalWrapper() {
        List<Krasnal> p = GetAllKrasnal();
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public List<Krasnal> GetAllKrasnal() {
        return krasnalRepo.GetKrasnalList();
    }

    @PostMapping("/new")
    public ResponseEntity<Krasnal> SaveNewKrasnalWrapper(@RequestBody KrasnalWeb newKrasnal, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Krasnal p = SaveNewKrasnal(newKrasnal);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Krasnal SaveNewKrasnal(KrasnalWeb newKrasnal) {
        System.out.println(newKrasnal);
        return krasnalRepo.AddNewKrasnal(newKrasnal);
    }

    @DeleteMapping("/delete/{krasnalId}")
    public ResponseEntity<Boolean> DeleteKrasnalWrapper(@PathVariable int krasnalId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Boolean p = DeleteKrasnal(krasnalId);
            if (p == null || !p) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Boolean DeleteKrasnal(int krasnalId) {
        return krasnalRepo.HideKrasnal(krasnalId);
    }

    @PatchMapping("/update/{krasnalId}")
    public ResponseEntity<Krasnal> UpdateKrasnalWrapper(@RequestBody KrasnalWeb krasnalToUpdate, @PathVariable int krasnalId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Krasnal p = UpdateKrasnal(krasnalToUpdate, krasnalId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Krasnal UpdateKrasnal(KrasnalWeb krasnalToUpdate, int krasnalId) {
        return krasnalRepo.UpdateKrasnal(krasnalId, krasnalToUpdate);
    }
}
