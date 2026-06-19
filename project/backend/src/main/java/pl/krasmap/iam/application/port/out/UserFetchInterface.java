package pl.krasmap.iam.application.port.out;

import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.common.data.UserRole;

import java.util.List;

public interface UserFetchInterface {
    void CheckDBConnection();
    User GetUser(int userId);
    User GetUser(String login);
    List<User> GetAllUsers();
    int SaveUser(UserWeb user);
    int UpdateUser(int userId, UserWeb user);
    boolean UpdateUserRole(int userId, UserRole role);
    boolean ActivateUser(int userId);
    boolean HideUser(int userId);
    boolean DeleteUser(int userId);
    String CheckUserPassword(String login);
}
