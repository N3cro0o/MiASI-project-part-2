package pl.krasmap.iam.application.port.out;

import pl.krasmap.iam.application.domain.UserWeb;
import pl.krasmap.iam.application.domain.user.User;

import java.util.List;

public interface UserFetchInterface {
    void CheckDBConnection();
    User GetUser(int userId);
    List<User> GetAllUsers();
    int SaveUser(UserWeb user);
    int UpdateUser(int userId, UserWeb user);
    boolean HideUser(int userId);
    boolean DeleteUser(int userId);
}
