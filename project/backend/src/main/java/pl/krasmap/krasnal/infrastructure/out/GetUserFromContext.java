package pl.krasmap.krasnal.infrastructure.out;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.krasmap.iam.application.port.in.UserGetInterface;
import pl.krasmap.krasnal.application.port.out.GetUserInterface;

@Component
public class GetUserFromContext implements GetUserInterface {

    private final UserGetInterface userFetch;

    public GetUserFromContext(@Lazy UserGetInterface user){
        userFetch = user;
    }

    @Override
    public String GetLoginFromUser(int userId) {
        return userFetch.GetUserUsername(userId);
    }
}
