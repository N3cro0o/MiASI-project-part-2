package pl.krasmap.submission.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.submission.application.domain.data.submission.SubmissionReview;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.domain.event.SubmissionAcceptedEvent;
import pl.krasmap.submission.application.port.out.GetKrasnalInterface;

import java.time.OffsetDateTime;

@Service
public class CheckSubmission {

    private final HoldSubmissionRepo subRepo;
    private final GetKrasnalInterface krasnalServ;
    private final ApplicationEventPublisher events;

    public CheckSubmission(HoldSubmissionRepo repo, GetKrasnalInterface krasnal, ApplicationEventPublisher e) {
        subRepo = repo;
        krasnalServ = krasnal;
        events = e;
    }

    public Pair<Submission, ReviewKrasnal> GetSubmissonPair(int subId){
        Submission sub = subRepo.GetSubmission(subId);
        System.out.println(sub);
        ReviewKrasnal kr = GenerateKrasnalFromJson(sub.json());
        return Pair.of(sub, kr);
    }

    public static ReviewKrasnal GenerateKrasnalFromJson(String json) {
        ReviewKrasnal obj = null;
        try {
            obj = Json.mapper().readValue(json, ReviewKrasnal.class);
        } catch (JsonProcessingException e) {
            System.err.println(e.toString());
        }
        return obj;
    }

    public boolean RejectSubmission(int userId, int subId, String reason) {
        SubmissionReview rev = SubmissionReview.newObject(userId, reason, OffsetDateTime.now());
        Submission sub = subRepo.GetSubmission(subId);
        if (userId == sub.userId()) return false;
        Submission newSub = sub.With(SubmissionStatus.Rejected, rev);
        return subRepo.UpdateSubReview(newSub);
    }

    public ReviewKrasnal AcceptSubmission(int userId, int subId) {
        SubmissionReview rev = SubmissionReview.newObject(userId, OffsetDateTime.now());
        Submission sub = subRepo.GetSubmission(subId);
        if (userId == sub.userId()) return null;
        Submission newSub = sub.With(SubmissionStatus.Accepted, rev);
        subRepo.UpdateSubReview(newSub);
        var k = GenerateKrasnalFromJson(sub.json());
        events.publishEvent(new SubmissionAcceptedEvent(subId, userId, k));
        return k;
    }

    public Boolean Check(int userId, int subId) {
        Submission sub = subRepo.GetSubmission(subId);
        return sub.userId() != userId;
    }
}
