package pl.krasmap.iam.application.port.out;

import pl.krasmap.iam.application.domain.User;

import java.util.List;

public interface UserFetchInterface {
    void CheckDBConnection();
    User GetUser(int userId);
    List<User> GetAllUsers();
}
