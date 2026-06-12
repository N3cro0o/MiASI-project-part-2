package pl.krasmap.common.auth.data;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.port.in.UserGetInterface;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;


@Service
public class UserAuthService implements UserAuthInterface {

    private static int TIMETOLIVE = 18_000; // Secs
    private final SecretKey secret_key;
    private UserGetInterface getUser;

    public UserAuthService(UserGetInterface users) {
        getUser = users;
        byte[] decodedKey = "LoremIpsumDolorSitAmetConsecteturAdipiscingElit".getBytes();
        this.secret_key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String GenerateJwt(int userId){
        return Jwts.builder().claim("id", userId).expiration(Date.from(Instant.now().plusSeconds(TIMETOLIVE)))
                .signWith(secret_key)
                .compact();
    }

    public int DecodeJwt(String jwt) {
        int id = 0;
        Claims p = Jwts.parser().verifyWith(secret_key).build().parseSignedClaims(jwt).getPayload();
        System.out.println(p.keySet());
        return p.get("id", Integer.class);
    }

    public boolean CheckAccess(String jwt, UserRole minRole) {
        int id = DecodeJwt(jwt);
        UserRole r = getUser.GetUserRole(id);
        return r.RoleCheck(minRole);
    }

}
