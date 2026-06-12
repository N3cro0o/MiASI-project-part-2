package pl.krasmap.iam.infrastructure.in;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.iam.application.domain.UserNew;
import pl.krasmap.iam.application.port.in.LoginInterface;
import pl.krasmap.iam.application.service.HoldUserRepo;
import pl.krasmap.iam.application.service.LoginService;
import pl.krasmap.interaction.application.domain.Review;

@RestController
@RequestMapping("/api/auth")
public class LoginWeb implements LoginInterface {

    private final LoginService loginService;

    public LoginWeb(LoginService login) {
        loginService = login;
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginWrapper(String loginOrEmail, String password) {
        String p = login(loginOrEmail, password);
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public String login(String loginOrEmail, String password) {
        var outcome = loginService.CheckLogin(loginOrEmail, password);
        return outcome.getRight();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerWrapper(@RequestBody UserNew newUser) {
        String p = register(newUser);
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public String register(UserNew newUser) {
        var outcome = loginService.Register(newUser);
        if (outcome.getLeft())
            return outcome.getRight();
        return "bajo jajo";
    }
}
