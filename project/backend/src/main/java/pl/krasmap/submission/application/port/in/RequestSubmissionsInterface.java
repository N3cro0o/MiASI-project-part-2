package pl.krasmap.submission.application.port.in;

import pl.krasmap.submission.application.domain.submission.Submission;

import java.util.List;

public interface RequestSubmissionsInterface {
    List<Submission> GetUserSubmissions(int userId);
}
