package pl.krasmap.iam.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.domain.event.UserCreatedEvent;

import java.util.List;

@Service
public class HandleUserService {

    private final HoldUserRepo repos;
    private final ApplicationEventPublisher events;

    public HandleUserService(HoldUserRepo r, ApplicationEventPublisher e){
        repos = r;
        events = e;
    }

    public User GetUser(int id) {
        return repos.GetUser(id);
    }

    public User GetUser(String login) {
        return repos.GetUser(login);
    }

    public String GetUserPass(String login) {
        return repos.GetUserPass(login);
    }

    public List<User> GetUserList() {
        return repos.GetUserList();
    }

    public User AddUser(UserWeb userToAdd) {
        int id = repos.AddUser(userToAdd);
        events.publishEvent(new UserCreatedEvent(id, userToAdd.role(), userToAdd.login()));
        return repos.GetUser(id);
    }

    public User UpdateUser(int userId, UserWeb userToAdd) {
        int id = repos.UpdateUser(userId, userToAdd);
        return repos.GetUser(id);
    }

    public boolean DeleteUser(int userId) {
        return repos.DeleteUser(userId);
    }
}
