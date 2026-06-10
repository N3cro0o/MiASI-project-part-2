package pl.krasmap.iam.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.User;
import pl.krasmap.iam.application.port.out.UserFetchInterface;
import pl.krasmap.iam.infrastructure.out.UserFetchPostgres;

import java.util.List;

@Service
public class HoldUserRepo {
    final private UserFetchInterface userFetch;

    public HoldUserRepo(UserFetchInterface fetch) {
        userFetch = fetch;
    }

    public User GetUser(int id) {
        return userFetch.GetUser(id);
    }

    public List<User> GetUserList() {
        return userFetch.GetAllUsers();
    }
}
