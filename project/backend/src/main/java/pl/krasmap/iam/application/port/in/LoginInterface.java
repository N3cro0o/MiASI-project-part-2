package pl.krasmap.iam.application.port.in;

import org.apache.commons.lang3.tuple.Pair;
import pl.krasmap.iam.application.domain.data.UserNew;

public interface LoginInterface {
    Pair<Boolean, String> login(String login, String password);
    Pair<Boolean, String> register(UserNew newUser);
}
