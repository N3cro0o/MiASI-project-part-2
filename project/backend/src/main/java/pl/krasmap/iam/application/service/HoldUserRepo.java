package pl.krasmap.iam.application.service;

import org.springframework.stereotype.Repository;
import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.common.data.UserRole;
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

    public int AddUser(UserWeb userToAdd) {
        return userFetch.SaveUser(userToAdd);
    }

    public int UpdateUser(int userId, UserWeb userToAdd) {
        return userFetch.UpdateUser(userId, userToAdd);
    }

    public boolean DeleteUser(int userId) {
        return userFetch.DeleteUser(userId);
    }

    public boolean HideUser(int userId) {
        return userFetch.HideUser(userId);
    }

    public boolean ActivateUser(int userId) {
        return userFetch.ActivateUser(userId);
    }

    public boolean UpdateUserRole(int userId, UserRole role) {
        return userFetch.UpdateUserRole(userId, role);
    }
}
