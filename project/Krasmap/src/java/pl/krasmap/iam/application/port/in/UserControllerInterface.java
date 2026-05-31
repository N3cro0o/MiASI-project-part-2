package pl.krasmap.iam.application.port.in;

import pl.krasmap.iam.application.domain.User;

public interface UserControllerInterface {
    User GetUser(int userId);
}
