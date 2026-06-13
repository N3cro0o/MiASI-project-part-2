package pl.krasmap.iam.application.port.out;

import pl.krasmap.iam.application.domain.stats.UserVisits;

import java.util.List;

public interface GetUserVisitsInterface {
    List<UserVisits> GetUserVisits(int userId);
}
