package pl.krasmap.submission.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.domain.data.NewSubmission;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.submission.application.domain.event.SubCreatedEvent;
import pl.krasmap.submission.application.domain.event.SubUpdatedEvent;

import java.util.List;

@Service
public class HandleSubmissionService {

    private final HoldSubmissionRepo subRepo;
    private final ApplicationEventPublisher events;

    public HandleSubmissionService(HoldSubmissionRepo r, ApplicationEventPublisher e){
        subRepo = r;
        events = e;
    }

    public Submission AddSubmission(NewSubmission submission) {
        int id = subRepo.AddSubmission(submission);
        var s = GetSubmission(id);
        if (s == null) {
            throw new IllegalStateException("Database insert failed");
        }
        events.publishEvent(new SubCreatedEvent(id, s.userId(), s.status()));
        return s;
    }

    public SubmissionStatus CheckSubmission(int subId) {
        return subRepo.CheckSubmission(subId);
    }

    public List<Submission> GetSubmissionsFromUser(int userId) {
        return subRepo.GetSubmissionsFromUser(userId);
    }

    public Submission GetSubmission(int subId) {
        return subRepo.GetSubmission(subId);
    }

    public boolean UpdateSubReview(Submission newSub) {
        return subRepo.UpdateSubReview(newSub);
    }

    public Submission UpdateSubmission(int subId, NewSubmission submission) {
        int id = subRepo.UpdateSubmission(subId, submission);
        var s = subRepo.GetSubmission(id);
        events.publishEvent(new SubUpdatedEvent(subId, s.userId(), s.status()));
        return s;
    }

    public List<Submission> GetAllSubmissions() {
        return subRepo.GetAllSubmissions();
    }

    public List<Submission> GetAllSubmissions(SubmissionStatus status) {
        return subRepo.GetAllSubmissions(status);
    }
}
