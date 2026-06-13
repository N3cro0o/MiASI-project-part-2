package pl.krasmap.iam.application.port.out;

import pl.krasmap.iam.application.domain.UserSubmission;

import java.util.List;

public interface GetUserSubmissionsInterface {
    List<UserSubmission> GetUserSubmissions(int userId);
}
