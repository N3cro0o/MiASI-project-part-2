package pl.krasmap.iam.application.service;

import org.springframework.stereotype.Repository;
import pl.krasmap.iam.application.domain.UserWeb;
import pl.krasmap.iam.application.domain.user.User;
import pl.krasmap.iam.application.port.out.UserFetchInterface;

import java.util.List;

@Repository
public class HoldUserRepo {
    final private UserFetchInterface userFetch;

    public HoldUserRepo(UserFetchInterface fetch) {
        userFetch = fetch;
    }

    public User GetUser(int id) {
        return userFetch.GetUser(id);
    }

    public User GetUser(String login) {
        return userFetch.GetUser(login);
    }

    public String GetUserPass(String login) {
        return userFetch.CheckUserPassword(login);
    }

    public List<User> GetUserList() {
        return userFetch.GetAllUsers();
    }

    public User AddUser(UserWeb userToAdd) {
        int id = userFetch.SaveUser(userToAdd);
        return userFetch.GetUser(id);
    }

    public User UpdateUser(int userId, UserWeb userToAdd) {
        int id = userFetch.UpdateUser(userId, userToAdd);
        return userFetch.GetUser(id);
    }

    public boolean DeleteUser(int userId) {
        return userFetch.HideUser(userId);
    }
}
