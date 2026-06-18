package pl.krasmap.interaction.infrastructure.in;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.interaction.application.domain.data.fav.NewVisit;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;
import pl.krasmap.interaction.application.port.in.VisitControllerInterface;
import pl.krasmap.interaction.application.service.HandleVisitedService;
import pl.krasmap.interaction.application.service.HoldVisitedRepo;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitControllerWeb implements VisitControllerInterface {

    private final HandleVisitedService visitHandle;
    private final UserAuthInterface auth;

    public VisitControllerWeb(HandleVisitedService visit, @Lazy UserAuthInterface authServ){
        visitHandle = visit;
        auth = authServ;
    }

    @GetMapping("/{visitedId}")
    public ResponseEntity<VisitedKrasnal> GetVisitAdminWrapper(@PathVariable int visitedId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            VisitedKrasnal p = GetVisit(visitedId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public VisitedKrasnal GetVisit(int visitedId) {
        return visitHandle.GetVisit(visitedId);
    }

    @GetMapping("/krasnal/{krasnalId}")
    public ResponseEntity<VisitedKrasnal> GetVisitWrapper(@PathVariable int krasnalId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            VisitedKrasnal p = GetVisit(userId, krasnalId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public VisitedKrasnal GetVisit(int userId, int krasnalId) {
        return visitHandle.GetVisit(userId, krasnalId);
    }

    @GetMapping("/krasnal/{krasnalId}/all")
    public ResponseEntity<List<VisitedKrasnal>> GetVisitsFromKrasnalWrapper(@PathVariable int krasnalId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            List<VisitedKrasnal> p = GetVisitsFromKrasnal(krasnalId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public List<VisitedKrasnal> GetVisitsFromKrasnal(int krasnalId) {
        return visitHandle.GetVisitsFromKrasnal(krasnalId);
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<List<VisitedKrasnal>> GetVisitsFromUserGlobalWrapper(@PathVariable int userId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            List<VisitedKrasnal> p = GetVisitsFromUser(userId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @GetMapping("/me")
    public ResponseEntity<List<Integer>> GetVisitsFromUserGlobalWrapper( @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            List<VisitedKrasnal> p = GetVisitsFromUser(userId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            List<Integer> krasnalIds = p.stream().map(VisitedKrasnal::krasnalId).toList();
            return new ResponseEntity<>(krasnalIds, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }


    @Override
    public List<VisitedKrasnal> GetVisitsFromUser(int userId) {
        return visitHandle.GetVisitsFromUser(userId);
    }

    @PostMapping("/{krasnalId}")
    public ResponseEntity<VisitedKrasnal> AddVisitWrapper(@PathVariable int krasnalId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            var v = new NewVisit(userId, krasnalId);
            VisitedKrasnal p = AddVisit(v);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public VisitedKrasnal AddVisit(NewVisit visit) {
        return visitHandle.AddVisit(visit);
    }

    @DeleteMapping("/admin/{visitedId}")
    public ResponseEntity<Boolean> RemoveVisitGlobalWrapper(@PathVariable int visitedId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            Boolean p = RemoveVisit(visitedId);
            if (p == null || !p) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Boolean RemoveVisit(int visitedId) {
        return visitHandle.RemoveVisit(visitedId);
    }

    @DeleteMapping("/{krasnalId}")
    public ResponseEntity<Boolean> RemoveVisitWrapper(@PathVariable int krasnalId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            Boolean p = RemoveVisit(userId, krasnalId);
            if (p == null || !p) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Boolean RemoveVisit(int userId, int krasnalId) {
        return visitHandle.RemoveVisit(userId, krasnalId);
    }
}
