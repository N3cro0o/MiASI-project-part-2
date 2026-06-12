package pl.krasmap.iam.infrastructure.in;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.iam.application.domain.UserNew;
import pl.krasmap.iam.application.port.in.LoginInterface;
import pl.krasmap.iam.application.service.HoldUserRepo;
import pl.krasmap.iam.application.service.LoginService;

@RestController
@RequestMapping("/api/auth")
public class LoginWeb implements LoginInterface {

    private final LoginService loginService;

    public LoginWeb(LoginService login) {
        loginService = login;
    }

    @Override
    @GetMapping("/login")
    public String login(String loginOrEmail, String password) {
        var outcome = loginService.CheckLogin(loginOrEmail, password);
        return outcome.getRight();
    }

    @Override
    @PostMapping("/register")
    public String register(@RequestBody UserNew newUser) {
        var outcome = loginService.Register(newUser);
        if (outcome.getLeft())
            return outcome.getRight();
        return "bajo jajo";
    }
}
