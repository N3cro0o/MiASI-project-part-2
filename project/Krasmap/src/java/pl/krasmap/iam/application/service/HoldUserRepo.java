package pl.krasmap.iam.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.User;
import pl.krasmap.iam.application.port.out.UserFetchInterface;
import pl.krasmap.iam.infrastructure.out.UserFetchPostgres;

import java.util.List;

@Service
public class HoldUserRepo {

    final private List<User> userList;

    public HoldUserRepo () {
        UserFetchInterface dbConn = new UserFetchPostgres();
        userList = dbConn.GetAllUsers();
    }

    public User GetUser(int id) {
        User obj = null;
        for (User k : userList) {
            if (k.id() == id) { obj = k; break; }
        }
        return obj;
    }

    public List<User> GetUserList() {
        return userList;
    }
}
