package pl.krasmap.iam.infrastructure.in;

import org.springframework.stereotype.Component;
import pl.krasmap.iam.application.port.in.UserGetInterface;
import pl.krasmap.iam.application.service.HoldUserRepo;

@Component
public class UserGetFromRepo implements UserGetInterface {

    private final HoldUserRepo userRepo;

    public UserGetFromRepo(HoldUserRepo repo) {
        userRepo = repo;
    }

    @Override
    public String GetUserUsername(int userId) {
        return userRepo.GetUser(userId).login();
    }
}
