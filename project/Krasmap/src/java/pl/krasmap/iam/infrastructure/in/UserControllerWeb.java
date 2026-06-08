package pl.krasmap.iam.infrastructure.in;

import org.springframework.web.bind.annotation.*;
import pl.krasmap.iam.application.domain.User;
import pl.krasmap.iam.application.port.in.UserControllerInterface;
import pl.krasmap.iam.infrastructure.out.UserFetchPostgres;

@RestController
@RequestMapping("/user")
public class UserControllerWeb implements UserControllerInterface {

    @Override
    @GetMapping("/{userId}")
    public User GetUser(@PathVariable int userId) {
        UserFetchPostgres dbConn = new UserFetchPostgres();
        return dbConn.GetUser(userId);
    }

    @Override
    @GetMapping("/db/conn/check")
    public void CheckDBConnection() {
        UserFetchPostgres dbConn = new UserFetchPostgres();
        dbConn.CheckDBConnection();
    }
}
