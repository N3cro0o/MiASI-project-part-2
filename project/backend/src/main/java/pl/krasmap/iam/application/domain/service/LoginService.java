package pl.krasmap.iam.application.domain.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.iam.application.domain.data.UserNew;
import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.iam.application.service.HandleUserService;

@Service
public class LoginService {
    private final HandleUserService userHandle;
    private final UserAuthInterface auth;

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);

    public LoginService(HandleUserService repo, @Lazy UserAuthInterface authServ) {
        userHandle = repo;
        auth = authServ;
    }

    public Pair<Boolean, String> CheckLogin(String login, String pass){
        User user = userHandle.GetUser(login);
        if (user == null) {
            throw new IllegalArgumentException("USER_NOT_FOUND");
        }
        if (!user.active()) {
            throw new IllegalArgumentException("ACCOUNT_BLOCKED");
        }
        
        String dbPass = userHandle.GetUserPass(login);
        boolean ch = bcrypt.matches(pass, dbPass);
        if (!ch) {
            throw new IllegalArgumentException("INVALID_PASSWORD");
        }
        
        String jwt = auth.GenerateJwt(user.id());
        return Pair.of(true, jwt);
    }

    public Pair<Boolean, String> Register(UserNew newUser) {
        UserWeb userToAdd = UserWeb.from(newUser, bcrypt.encode(newUser.password()));
        System.out.println(userToAdd);
        User u = userHandle.AddUser(userToAdd);
        System.out.println(u);
        if (u == null) {
            return Pair.of(false, "");
        }
        return Pair.of(true, auth.GenerateJwt(u.id()));
    }
}
