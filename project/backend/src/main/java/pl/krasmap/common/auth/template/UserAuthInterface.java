package pl.krasmap.common.auth.template;

import pl.krasmap.common.data.UserRole;

public interface UserAuthInterface {
    String GenerateJwt(int userId);
    int DecodeJwt(String jwt);
    boolean CheckAccess(String jwt, UserRole minRole);
}
