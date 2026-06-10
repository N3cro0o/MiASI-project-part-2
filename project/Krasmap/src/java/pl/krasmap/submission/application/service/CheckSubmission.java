package pl.krasmap.submission.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import pl.krasmap.submission.application.domain.Krasnal;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.domain.submission.SubmissionReview;
import pl.krasmap.submission.application.domain.submission.SubmissionStatus;

import java.time.OffsetDateTime;

@Service
public class CheckSubmission {

    private final HoldSubmissionRepo subRepo;

    public CheckSubmission(HoldSubmissionRepo repo) {
        subRepo = repo;
    }

    public Pair<Submission, Krasnal> GetSubmissonPair(int subId){
        Submission sub = subRepo.GetSubmission(subId);
        System.out.println(sub);
        Krasnal kr = GenerateKrasnalFromJson(sub.json());
        return Pair.of(sub, kr);
    }

    public Krasnal GenerateKrasnalFromJson(String json) {
        Krasnal obj = null;
        try {
            obj = Json.mapper().readValue(json, Krasnal.class);
        } catch (JsonProcessingException e) {
            System.err.println(e.toString());
        }
        return obj;
    }

    public boolean RejectSubmission(int userId, int subId, String reason) {
        SubmissionReview rev = SubmissionReview.newObject(userId, reason, OffsetDateTime.now());
        Submission sub = subRepo.GetSubmission(subId);
        Submission newSub = sub.With(SubmissionStatus.Rejected, rev);
        return subRepo.UpdateSubReview(newSub);
    }

    public Krasnal AcceptSubmission(int userId, int subId) {
        SubmissionReview rev = SubmissionReview.newObject(userId, OffsetDateTime.now());
        Submission sub = subRepo.GetSubmission(subId);
        Submission newSub = sub.With(SubmissionStatus.Accepted, rev);
        subRepo.UpdateSubReview(newSub);
        return GenerateKrasnalFromJson(sub.json());
    }
}
