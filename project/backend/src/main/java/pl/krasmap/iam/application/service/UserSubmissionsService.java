package pl.krasmap.iam.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.UserSubmission;
import pl.krasmap.iam.application.port.out.GetUserReviewsInterface;

import java.util.List;

@Service
public class UserSubmissionsService {
    private final GetUserReviewsInterface userSubs;

    public UserSubmissionsService(GetUserReviewsInterface subs) {
        userSubs = subs;
    }

    public List<UserSubmission> GetUserSubs(int userId) {
        return userSubs.GetUserSubmissions(userId);
    }
}
