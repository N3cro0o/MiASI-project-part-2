package pl.krasmap.iam.application.port.in;

import org.apache.commons.lang3.tuple.Pair;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.iam.application.domain.data.stats.UserStats;
import pl.krasmap.iam.application.domain.data.stats.UserSubmission;
import pl.krasmap.iam.application.domain.data.UserWeb;

import java.util.List;

public interface UserControllerInterface {
    Pair<User, String> GetUser(int userId);
    List<User> GetUserList();
    void CheckDBConnection();
    User AddUser(UserWeb userToAdd);
    User UpdateUser(int userId, UserWeb userToUpdate);
    boolean RemoveUser(int userId);
    List<UserSubmission> GetUserSubmissions(int userId);
    UserStats GetUserStats(int userId);
}
