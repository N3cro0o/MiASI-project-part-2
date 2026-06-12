package pl.krasmap.iam.application.port.in;

import org.apache.commons.lang3.tuple.Pair;
import pl.krasmap.iam.application.domain.user.User;
import pl.krasmap.iam.application.domain.UserWeb;

import java.util.List;

public interface UserControllerInterface {
    Pair<User, String> GetUser(int userId);
    List<User> GetUserList();
    void CheckDBConnection();
    User AddUser(UserWeb userToAdd);
    User UpdateUser(int userId, UserWeb userToUpdate);
    boolean RemoveUser(int userId);
}
