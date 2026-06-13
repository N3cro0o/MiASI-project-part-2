package pl.krasmap.iam.application.port.in;

import pl.krasmap.common.data.UserRole;

public interface UserGetInterface {
    String GetUserUsername(int userId);
    UserRole GetUserRole(int userId);
}
