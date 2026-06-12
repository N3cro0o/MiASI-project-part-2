package pl.krasmap.common.auth.data;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.iam.application.port.in.UserGetInterface;

import java.time.Instant;
import java.util.Date;


@Service
public class UserAuthService implements UserAuthInterface {
    private static int TIMETOLIVE = 18_000; // Secs

    private UserGetInterface getUser;

    public UserAuthService(UserGetInterface users) {
        getUser = users;
    }

    public String GenerateJwt(int userId){
        return Jwts.builder().claim("id", userId).expiration(Date.from(Instant.now().plusSeconds(TIMETOLIVE)))
                .compact();
    }
}
