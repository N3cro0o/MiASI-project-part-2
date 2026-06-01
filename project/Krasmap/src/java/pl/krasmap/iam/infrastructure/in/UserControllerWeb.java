package pl.krasmap.iam.infrastructure.in;

import org.springframework.web.bind.annotation.*;
import pl.krasmap.iam.application.domain.User;
import pl.krasmap.iam.application.port.in.UserControllerInterface;

@RestController
@RequestMapping("/user")
public class UserControllerWeb implements UserControllerInterface {

    @Override
    @GetMapping("/{userId}")
    public User GetUser(@PathVariable int userId) {
        return User.dummy();
    }
}
