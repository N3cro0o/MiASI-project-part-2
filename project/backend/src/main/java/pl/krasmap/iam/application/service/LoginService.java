package pl.krasmap.iam.application.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.iam.application.domain.data.UserNew;
import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.domain.data.User;

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
        String dbPass = userHandle.GetUserPass(login);
        String jwt = "" ;
        boolean ch = bcrypt.matches(pass, dbPass);
        if (ch) {
            jwt = auth.GenerateJwt(userHandle.GetUser(login).id());
        }
        return Pair.of(false, jwt);
    }

    public Pair<Boolean, String> Register(UserNew newUser) {
        UserWeb userToAdd = UserWeb.from(newUser, bcrypt.encode(newUser.password()));
        System.out.println(userToAdd);
        User u = userHandle.AddUser(userToAdd);
        System.out.println(u);
        return Pair.of(!u.isNull(), auth.GenerateJwt(u.id()));
    }
}
