package pl.krasmap.submission.application.port.in;

import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.data.submission.Submission;

import java.util.List;

public interface RequestSubmissionsInterface {
    List<Submission> GetUserSubmissions(int userId);
    ReviewKrasnal ParseKrasnalFromJson(String json);
}
