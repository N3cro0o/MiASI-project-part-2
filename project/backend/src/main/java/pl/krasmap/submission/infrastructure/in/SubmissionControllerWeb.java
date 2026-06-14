package pl.krasmap.submission.infrastructure.in;


import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.common.data.RejectReason;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.submission.application.domain.ReviewKrasnal;
import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.SubmissionReturn;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.port.in.SubmissionControllerInterface;
import pl.krasmap.submission.application.service.CheckSubmission;
import pl.krasmap.submission.application.service.HoldSubmissionRepo;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionControllerWeb implements SubmissionControllerInterface {

    private final HoldSubmissionRepo subRepo;
    private final CheckSubmission subCheck;
    private final UserAuthInterface auth;

    public SubmissionControllerWeb(HoldSubmissionRepo repo, CheckSubmission check, @Lazy UserAuthInterface authServ) {
        subRepo = repo;
        subCheck = check;
        auth = authServ;
    }

    @PostMapping
    public ResponseEntity<Submission> PostSubmissionWrapper(@RequestBody ReviewKrasnal submission, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            var sub = new NewSubmission(userId, submission);
            Submission p = PostSubmission(sub);
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

    @GetMapping("/user/{userId}")
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

    @GetMapping("/{subId}")
    public ResponseEntity<Pair<Submission, ReviewKrasnal>> GetSubmissionWrapper(@PathVariable int subId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Pair<Submission, ReviewKrasnal> p = GetSubmission(subId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Pair<Submission, ReviewKrasnal> GetSubmission(int subId) {
        return subCheck.GetSubmissonPair(subId);
    }

    @PatchMapping("/reject/{subId}")
    public ResponseEntity<Boolean> RejectSubmissionWrapper(@PathVariable int subId, @RequestBody RejectReason reason, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            Boolean p = RejectSubmission(userId, subId, reason.reason());
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
    public ResponseEntity<ReviewKrasnal> AcceptSubmissionWrapper(@PathVariable int subId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            ReviewKrasnal p = AcceptSubmission(userId, subId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ReviewKrasnal AcceptSubmission(int userId, int subId) {
        return subCheck.AcceptSubmission(userId, subId);
    }

    @PatchMapping("/{subId}")
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

    @GetMapping
    public ResponseEntity<List<SubmissionReturn>> GetAllSubmissionsWrapper(@RequestParam(required = false) String status,
                                                                     @RequestHeader("Authorization") String jwt)
    {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Editor);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            List<SubmissionReturn> p;
            if (status != null) {
                p = GetAllSubmissions(SubmissionStatus.FromString(status));
            }
            else {
                p = GetAllSubmissions();
            }
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public List<SubmissionReturn> GetAllSubmissions() {
        var l =  subRepo.GetAllSubmissions();
        List<SubmissionReturn> t = new ArrayList<>();
        for (Submission s : l) {
            ReviewKrasnal k = subCheck.GenerateKrasnalFromJson(s.json());
            String rejectReason = s.review() != null ? s.review().rejectionReason() : null;
            t.add(new SubmissionReturn(s.id(), s.userId(),s.status(), s.submittedTime(), k.name(), k.position(), rejectReason, k.description()));
        }
        return t;
    }

    @Override
    public List<SubmissionReturn> GetAllSubmissions(SubmissionStatus status) {
        var l =  subRepo.GetAllSubmissions(status);
        List<SubmissionReturn> t = new ArrayList<>();
        for (Submission s : l) {
            ReviewKrasnal k = subCheck.GenerateKrasnalFromJson(s.json());
            String rejectReason = s.review() != null ? s.review().rejectionReason() : null;
            t.add(new SubmissionReturn(s.id(), s.userId(),s.status(), s.submittedTime(), k.name(), k.position(), rejectReason, k.description()));
        }
        return t;
    }


    @GetMapping("/editor/check/{subId}")
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
