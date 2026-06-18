package pl.krasmap.iam.infrastructure.in;

import org.springframework.stereotype.Component;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.port.in.UserGetInterface;
import pl.krasmap.iam.application.service.HandleUserService;
import pl.krasmap.iam.application.service.HoldUserRepo;

@Component
public class UserGetFromRepo implements UserGetInterface {

    private final HandleUserService userHandle;

    public UserGetFromRepo(HandleUserService repo) {
        userHandle = repo;
    }

    @Override
    public String GetUserUsername(int userId) {
        return userHandle.GetUser(userId).login();
    }

    @Override
    public UserRole GetUserRole(int userId) {
        return userHandle.GetUser(userId).role();
    }
}
