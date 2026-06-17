package pl.krasmap.krasnal.infrastructure.in;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.krasnal.application.domain.data.KrasnalReview;
import pl.krasmap.krasnal.application.domain.data.KrasnalReviewWeb;
import pl.krasmap.krasnal.application.domain.data.KrasnalWeb;
import pl.krasmap.krasnal.application.domain.data.krasnal.Krasnal;
import pl.krasmap.krasnal.application.port.in.KrasnalControllerInterface;
import pl.krasmap.krasnal.application.port.out.GetKrasnalReviewInterface;
import pl.krasmap.krasnal.application.service.KrasnalHandleService;

import java.util.List;

@RestController
@RequestMapping("/api/krasnals")
public class KrasnalControllerWeb implements KrasnalControllerInterface {

    private final KrasnalHandleService krasnalHandle;
    private final GetKrasnalReviewInterface krasnalReviews;
    private final UserAuthInterface auth;

    KrasnalControllerWeb(KrasnalHandleService handle, GetKrasnalReviewInterface review, @Lazy UserAuthInterface authServ) {
        krasnalReviews = review;
        krasnalHandle = handle;
        auth = authServ;
    }

    @GetMapping("/{krasnalId}")
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
        return krasnalHandle.GetKrasnal(krasnalId);
    }

    @GetMapping("/{krasnalId}/reviews")
    public ResponseEntity<List<KrasnalReview>> GetKrasnalReviewsWrapper(@PathVariable int krasnalId) {
        List<KrasnalReview> p = GetKrasnalReviews(krasnalId);
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public List<KrasnalReview> GetKrasnalReviews(int krasnalId) {
        return krasnalReviews.GetAllReviews(krasnalId);
    }

    @PostMapping("/{krasnalId}/reviews")
    public ResponseEntity<KrasnalReview> GetKrasnalReviewsWrapper(@PathVariable int krasnalId,
                                                                        @RequestBody KrasnalReviewWeb review,
                                                                        @RequestHeader("Authorization") String jwt)
    {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            KrasnalReview p = AddKrasnalReview(krasnalId, userId, review);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.CONFLICT);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public KrasnalReview AddKrasnalReview(int krasnalId, int userId, KrasnalReviewWeb review) {
        return krasnalReviews.AddReview(krasnalId, userId, review);
    }

    @GetMapping
    public ResponseEntity<List<Krasnal>> GetAllKrasnalWrapper() {
        List<Krasnal> p = GetAllKrasnal();
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public List<Krasnal> GetAllKrasnal() {
        return krasnalHandle.GetKrasnalList();
    }

    @PostMapping("")
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
        return krasnalHandle.AddNewKrasnal(newKrasnal);
    }

    @DeleteMapping("/{krasnalId}")
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
        return krasnalHandle.HideKrasnal(krasnalId);
    }

    @DeleteMapping("/{krasnalId}/force")
    public ResponseEntity<Boolean> DeleteForceKrasnalWrapper(@PathVariable int krasnalId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Boolean p = DeleteForceKrasnal(krasnalId);
            if (p == null || !p) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Boolean DeleteForceKrasnal(int krasnalId) {
        return krasnalHandle.DestroyKrasnal(krasnalId);
    }

    @PatchMapping("/{krasnalId}")
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
        return krasnalHandle.UpdateKrasnal(krasnalId, krasnalToUpdate);
    }
}
