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
    private final UserGetInterface getUser;

    public UserAuthService(UserGetInterface users) {
        getUser = users;
        byte[] decodedKey = "LoremIpsumDolorSitAmetConsecteturAdipiscingElit".getBytes();
        this.secret_key = Keys.hmacShaKeyFor(decodedKey);
    }

    @Override
    public String GenerateJwt(int userId){
        return Jwts.builder().claim("id", userId).expiration(Date.from(Instant.now().plusSeconds(TIMETOLIVE)))
                .signWith(secret_key)
                .compact();
    }

    @Override
    public int DecodeJwt(String jwt) {
        int id = 0;
        Claims p = Jwts.parser().verifyWith(secret_key).build().parseSignedClaims(jwt).getPayload();
        return p.get("id", Integer.class);
    }

    @Override
    public Boolean CheckAccess(String jwt, UserRole minRole) {
        try {
            int id = DecodeJwt(jwt);
            UserRole r = getUser.GetUserRole(id);
            return r.RoleCheck(minRole);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

}
