package pl.krasmap.iam.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.UserStats;
import pl.krasmap.iam.application.port.out.GetUserReviewsInterface;
import pl.krasmap.iam.application.port.out.GetUserSubmissionsInterface;

@Service
public class UserStatsService {
    private final GetUserReviewsInterface userReviews;
    private final GetUserSubmissionsInterface userSubs;

    public UserStatsService(GetUserSubmissionsInterface submissions, GetUserReviewsInterface revs){
        userReviews = revs;
        userSubs = submissions;
    }

    public UserStats GetUserStats(int userId){
        int subCount = userSubs.GetUserSubmissions(userId).size();
        int revCount = userReviews.GetUserReviews(userId).size();

        return new UserStats(userId, revCount, subCount, 0);
    }
}
