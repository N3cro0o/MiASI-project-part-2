package pl.krasmap.iam.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.User;

@Service
public class HoldUserRepo {

    public User GetUser(int _id) {
        return User.dummy();
    }
}
