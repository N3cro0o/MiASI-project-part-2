package pl.krasmap.submission.infrastructure.in;


import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.*;
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

    public SubmissionControllerWeb(HoldSubmissionRepo repo, CheckSubmission check) {
        subRepo = repo;
        subCheck = check;
    }

    @Override
    @PostMapping("/new")
    public Submission PostSubmission(@RequestBody NewSubmission submission) {
        return subRepo.AddSubmission(submission);
    }

    @Override
    @GetMapping("/check/{subId}")
    public SubmissionStatus CheckSubmission(@PathVariable int subId) {
        return subRepo.CheckSubmission(subId);
    }

    @Override
    @GetMapping("/get/user/{userId}")
    public List<Submission> GetSubmissionsFromUser(@PathVariable int userId) {
        return subRepo.GetSubmissionsFromUser(userId);
    }

    @Override
    @GetMapping("/get/{subId}")
    public Pair<Submission, Krasnal> GetSubmission(@PathVariable int subId) {
        return subCheck.GetSubmissonPair(subId);
    }

    @Override
    public boolean RejectSubmission(int userId, int subId, String reason) {
        return subCheck.RejectSubmission(userId, subId, reason);
    }

    @Override
    public Krasnal AcceptSubmission(int userId, int subId) {
        return subCheck.AcceptSubmission(userId, subId);
    }

    @Override
    public Submission UpdateSubmission(int subId, NewSubmission submission) {
        return subRepo.UpdateSubmission(subId, submission);
    }
}
