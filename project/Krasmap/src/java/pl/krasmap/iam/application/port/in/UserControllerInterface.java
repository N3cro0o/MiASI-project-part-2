package pl.krasmap.iam.application.port.in;

import pl.krasmap.iam.application.domain.User;

import java.util.List;

public interface UserControllerInterface {
    User GetUser(int userId);
    List<User> GetUserList();
    void CheckDBConnection();
}
