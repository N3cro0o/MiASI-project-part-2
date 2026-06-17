package pl.krasmap.iam.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.data.stats.UserSubmission;
import pl.krasmap.iam.application.port.out.GetUserSubmissionsInterface;

import java.util.List;

@Service
public class UserSubmissionsService {
    private final GetUserSubmissionsInterface userSubs;

    public UserSubmissionsService(GetUserSubmissionsInterface subs) {
        userSubs = subs;
    }

    public List<UserSubmission> GetUserSubs(int userId) {
        return userSubs.GetUserSubmissions(userId);
    }
}
