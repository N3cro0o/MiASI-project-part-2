package pl.krasmap.submission.infrastructure.in;

import org.springframework.web.bind.annotation.*;
import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.submission.SumbissionStatus;
import pl.krasmap.submission.application.port.in.SubmissionControllerInterface;

@RestController
@RequestMapping("/api/submission")
public class SubmissionControllerWeb implements SubmissionControllerInterface {
    @Override
    @PostMapping("/new")
    public void PostSubmission(@RequestBody NewSubmission submission) {
        System.out.println(submission);
    }

    @Override
    @GetMapping("/check/{subId}")
    public SumbissionStatus CheckSubmission(@PathVariable int subId) {
        return null;
    }
}
