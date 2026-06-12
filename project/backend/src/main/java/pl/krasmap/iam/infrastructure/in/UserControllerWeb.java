package pl.krasmap.iam.infrastructure.in;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.data.UserAuthService;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.iam.application.domain.UserWeb;
import pl.krasmap.iam.application.domain.user.User;
import pl.krasmap.iam.application.port.in.UserControllerInterface;
import pl.krasmap.iam.application.service.HoldUserRepo;
import pl.krasmap.iam.infrastructure.out.UserFetchPostgres;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserControllerWeb implements UserControllerInterface {

    private final HoldUserRepo userRepo;
    private final UserAuthInterface auth;

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);

    public UserControllerWeb(HoldUserRepo repo, @Lazy UserAuthInterface authServ) { /*Dzięki temu małemu skurwysynowi @Lazy cała autoryzacja zaczyna nie niszczyć aplikacji. JAK NIE WIEM*/
        userRepo = repo;
        auth = authServ;
    }

    @Override
    @GetMapping("/get/{userId}")
    public Pair<User, String> GetUser(@PathVariable int userId) {
        User u = userRepo.GetUser(userId);
        System.out.println(u.isNull());
        return Pair.of(u, auth.GenerateJwt(userId));
    }

    @Override
    @GetMapping("/get/all")
    public List<User> GetUserList(){
        return userRepo.GetUserList();
    }

    @Override
    @GetMapping("/db/conn/check")
    public void CheckDBConnection() {
        UserFetchPostgres dbConn = new UserFetchPostgres();
        dbConn.CheckDBConnection();
    }

    @Override
    @PostMapping("/add")
    public User AddUser(UserWeb userToAdd) {
        userToAdd = UserWeb.from(userToAdd, bcrypt.encode(userToAdd.password()));
        System.out.println(userToAdd);
        return userRepo.AddUser(userToAdd);
    }

    @Override
    @PatchMapping("/update/{userId}")
    public User UpdateUser(int userId, UserWeb userToUpdate) {
        userToUpdate = UserWeb.from(userToUpdate, bcrypt.encode(userToUpdate.password()));
        System.out.println(userToUpdate);
        return userRepo.UpdateUser(userId, userToUpdate);
    }

    @Override
    @DeleteMapping("/delete/{userId}")
    public boolean RemoveUser(int userId) {
        return userRepo.DeleteUser(userId);
    }
}
