package pl.krasmap.iam.infrastructure.in;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.iam.application.domain.data.LoginData;
import pl.krasmap.iam.application.domain.data.UserNew;
import pl.krasmap.iam.application.port.in.LoginInterface;
import pl.krasmap.iam.application.service.LoginService;

@RestController
@RequestMapping("/api/auth")
public class LoginWeb implements LoginInterface {

    private final LoginService loginService;

    public LoginWeb(LoginService login) {
        loginService = login;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginWrapper(@RequestBody LoginData loginData) {
        Pair<Boolean, String> p = login(loginData.loginOrEmail(), loginData.password());
        if (p == null || !p.getLeft()) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(p.getRight(), HttpStatus.valueOf(200));
    }

    @Override
    public Pair<Boolean, String> login(String loginOrEmail, String password) {
        return loginService.CheckLogin(loginOrEmail, password);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerWrapper(@RequestBody UserNew newUser) {
        Pair<Boolean, String> p = register(newUser);
        if (p == null || !p.getLeft()) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p.getRight(), HttpStatus.valueOf(200));
    }

    @Override
    public Pair<Boolean, String> register(UserNew newUser) {
        return loginService.Register(newUser);
    }
}
