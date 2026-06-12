package pl.krasmap.iam.application.port.in;

import pl.krasmap.iam.application.domain.UserNew;

public interface LoginInterface {
    String login(String login, String password);
    String register(UserNew newUser);
}
