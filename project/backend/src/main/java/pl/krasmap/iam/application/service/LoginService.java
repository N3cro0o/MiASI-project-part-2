package pl.krasmap.iam.application.service;


import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krasmap.common.auth.template.UserAuthInterface;

@Service
public class LoginService {
    private final HoldUserRepo userRepo;
    private final UserAuthInterface auth;

    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);

    public LoginService(HoldUserRepo repo, @Lazy UserAuthInterface authServ) {
        userRepo = repo;
        auth = authServ;
    }

    public Pair<Boolean, String> CheckLogin(String login, String pass){
        String dbPass = userRepo.GetUserPass(login);
        String jwt = "" ;
        boolean ch = bcrypt.matches(pass, dbPass);
        if (ch) {
            jwt = auth.GenerateJwt(userRepo.GetUser(login).id());
        }
        return Pair.of(false, jwt);
    }
}
