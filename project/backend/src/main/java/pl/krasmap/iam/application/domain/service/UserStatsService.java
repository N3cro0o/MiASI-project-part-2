package pl.krasmap.iam.application.domain.service;

import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.data.stats.UserStats;
import pl.krasmap.iam.application.port.out.GetUserReviewsInterface;
import pl.krasmap.iam.application.port.out.GetUserSubmissionsInterface;
import pl.krasmap.iam.application.port.out.GetUserVisitsInterface;

@Service
public class UserStatsService {
    private final GetUserReviewsInterface userReviews;
    private final GetUserSubmissionsInterface userSubs;
    private final GetUserVisitsInterface userVisits;

    public UserStatsService(GetUserSubmissionsInterface submissions,
                            GetUserReviewsInterface revs,
                            GetUserVisitsInterface visit)
    {
        userReviews = revs;
        userSubs = submissions;
        userVisits = visit;
    }

    public UserStats GetUserStats(int userId){
        int subCount = userSubs.GetUserSubmissions(userId).size();
        int revCount = userReviews.GetUserReviews(userId).size();
        int visCount = userVisits.GetUserVisits(userId).size();
        return new UserStats(userId, revCount, subCount, visCount);
    }
}
