package pl.krasmap.submission.infrastructure.in;


import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.krasnal.application.domain.KrasnalWeb;
import pl.krasmap.submission.application.domain.Krasnal;
import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.domain.submission.SubmissionStatus;
import pl.krasmap.submission.application.port.in.SubmissionControllerInterface;
import pl.krasmap.submission.application.service.CheckSubmission;
import pl.krasmap.submission.application.service.HoldSubmissionRepo;

import java.util.List;

@RestController
@RequestMapping("/api/submission")
public class SubmissionControllerWeb implements SubmissionControllerInterface {

    private final HoldSubmissionRepo subRepo;
    private final CheckSubmission subCheck;
    private final UserAuthInterface auth;

    public SubmissionControllerWeb(HoldSubmissionRepo repo, CheckSubmission check, @Lazy UserAuthInterface authServ) {
        subRepo = repo;
        subCheck = check;
        auth = authServ;
    }

    @PostMapping("/new")
    public ResponseEntity<Submission> PostSubmissionWrapper(@RequestBody NewSubmission submission, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Submission p = PostSubmission(submission);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Submission PostSubmission(NewSubmission submission) {
        return subRepo.AddSubmission(submission);
    }

    @GetMapping("/check/{subId}")
    public ResponseEntity<SubmissionStatus> CheckSubmissionWrapper(@PathVariable int subId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            SubmissionStatus p = CheckSubmission(subId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public SubmissionStatus CheckSubmission(int subId) {
        return subRepo.CheckSubmission(subId);
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<List<Submission>> GetSubmissionsFromUserWrapper(@PathVariable int userId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            List<Submission> p = GetSubmissionsFromUser(userId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public List<Submission> GetSubmissionsFromUser(int userId) {
        return subRepo.GetSubmissionsFromUser(userId);
    }

    @GetMapping("/get/{subId}")
    public ResponseEntity<Pair<Submission, Krasnal>> GetSubmissionWrapper(@PathVariable int subId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Pair<Submission, Krasnal> p = GetSubmission(subId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Pair<Submission, Krasnal> GetSubmission(int subId) {
        return subCheck.GetSubmissonPair(subId);
    }

    @PatchMapping("/reject/{subId}")
    public ResponseEntity<Boolean> RejectSubmissionWrapper(@PathVariable int subId, @RequestBody String reason, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            Boolean p = RejectSubmission(userId, subId, reason);
            if (p == null || !p) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Boolean RejectSubmission(int userId, int subId, String reason) {
        return subCheck.RejectSubmission(userId, subId, reason);
    }

    @PostMapping("/accept/{subId}")
    public ResponseEntity<Krasnal> AcceptSubmissionWrapper(@PathVariable int subId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            Krasnal p = AcceptSubmission(userId, subId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Krasnal AcceptSubmission(int userId, int subId) {
        return subCheck.AcceptSubmission(userId, subId);
    }

    @PatchMapping("/update/{subId}")
    public ResponseEntity<Submission> UpdateSubmissionWrapper(@PathVariable int subId,
                                                                             @RequestBody NewSubmission submission,
                                                                             @RequestHeader("Authorization") String jwt)
    {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Submission p = UpdateSubmission(subId, submission);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Submission UpdateSubmission(int subId, NewSubmission submission) {
        return subRepo.UpdateSubmission(subId, submission);
    }

    @GetMapping("/subs")
    public ResponseEntity<List<Submission>> GetAllSubmissionsWrapper(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            List<Submission> p = GetAllSubmissions();
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public List<Submission> GetAllSubmissions() {
        return subRepo.GetAllSubmissions();
    }


    @GetMapping("/check/{subId}")
    public ResponseEntity<Boolean> CanAcceptSubmissionWrapper(@PathVariable int subId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            Boolean p = CanAcceptSubmission(subId, userId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Boolean CanAcceptSubmission(int userId, int subId) {
        return subCheck.Check(userId, subId);
    }
}
