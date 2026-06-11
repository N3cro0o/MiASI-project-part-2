package pl.krasmap.iam.infrastructure.in;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
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

    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);

    public UserControllerWeb(HoldUserRepo repo) {
        userRepo = repo;
    }

    @Override
    @GetMapping("/get/{userId}")
    public User GetUser(@PathVariable int userId) {
        return userRepo.GetUser(userId);
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
    @PostMapping("/register")
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
