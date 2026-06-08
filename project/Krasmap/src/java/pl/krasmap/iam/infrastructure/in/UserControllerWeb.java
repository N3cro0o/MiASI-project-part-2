package pl.krasmap.iam.infrastructure.in;

import org.springframework.web.bind.annotation.*;
import pl.krasmap.iam.application.domain.User;
import pl.krasmap.iam.application.port.in.UserControllerInterface;
import pl.krasmap.iam.application.service.HoldUserRepo;
import pl.krasmap.iam.infrastructure.out.UserFetchPostgres;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserControllerWeb implements UserControllerInterface {

    private final HoldUserRepo userRepo;

    public UserControllerWeb(HoldUserRepo repo) {
        userRepo = repo;
    }

    @Override
    @GetMapping("/{userId}")
    public User GetUser(@PathVariable int userId) {
        return userRepo.GetUser(userId);
    }

    @Override
    @GetMapping("/all")
    public List<User> GetUserList(){
        return userRepo.GetUserList();
    }

    @Override
    @GetMapping("/db/conn/check")
    public void CheckDBConnection() {
        UserFetchPostgres dbConn = new UserFetchPostgres();
        dbConn.CheckDBConnection();
    }
}
