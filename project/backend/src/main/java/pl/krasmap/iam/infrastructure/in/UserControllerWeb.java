package pl.krasmap.iam.infrastructure.in;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.domain.stats.UserStats;
import pl.krasmap.iam.application.domain.stats.UserSubmission;
import pl.krasmap.iam.application.domain.UserWeb;
import pl.krasmap.iam.application.domain.User;
import pl.krasmap.iam.application.port.in.UserControllerInterface;
import pl.krasmap.iam.application.service.HoldUserRepo;
import pl.krasmap.iam.application.service.UserStatsService;
import pl.krasmap.iam.application.service.UserSubmissionsService;
import pl.krasmap.iam.infrastructure.out.UserFetchPostgres;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserControllerWeb implements UserControllerInterface {

    private final HoldUserRepo userRepo;
    private final UserAuthInterface auth;
    private final UserSubmissionsService userSubs;
    private final UserStatsService userStats;

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);

    public UserControllerWeb(HoldUserRepo repo, UserSubmissionsService subs, UserStatsService stat, @Lazy UserAuthInterface authServ) { /*Dzięki temu małemu skurwysynowi @Lazy cała autoryzacja zaczyna nie niszczyć aplikacji. JAK NIE WIEM*/
        userRepo = repo;
        auth = authServ;
        userSubs = subs;
        userStats = stat;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> GetUserWrapper(@PathVariable int userId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            User p = GetUser(userId).getLeft();
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @GetMapping("/me")
    public ResponseEntity<User> GetUserWrapper(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            User p = GetUser(userId).getLeft();
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @GetMapping("/me/restore_token")
    public ResponseEntity<String> GetUserJwtWrapper(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            String p = GetUser(userId).getRight();
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Pair<User, String> GetUser(int userId) {
        User u = userRepo.GetUser(userId);
        System.out.println(u.isNull());
        return Pair.of(u, auth.GenerateJwt(userId));
    }

    @GetMapping
    public ResponseEntity<List<User>> GetUserListWrapper(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            List<User> p = GetUserList();
            if (p.isEmpty()) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public List<User> GetUserList(){
        return userRepo.GetUserList();
    }

    @Override
    @GetMapping("/db/conn/check")
    public void CheckDBConnection() {
        UserFetchPostgres dbConn = new UserFetchPostgres();
        dbConn.CheckDBConnection();
    }

    @PostMapping
    public ResponseEntity<User> AddUserWrapper(@RequestBody UserWeb userToAdd, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            User p = AddUser(userToAdd);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public User AddUser(UserWeb userToAdd) {
        userToAdd = UserWeb.from(userToAdd, bcrypt.encode(userToAdd.password()));
        return userRepo.AddUser(userToAdd);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> UpdateUserWrapper(@PathVariable int userId, @RequestBody UserWeb userToUpdate, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            User p = UpdateUser(userId, userToUpdate);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @PatchMapping("/me")
    public ResponseEntity<User> UpdateSelfWrapper(@RequestBody UserWeb userToUpdate, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            User p = UpdateUser(userId, userToUpdate);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public User UpdateUser(int userId, UserWeb userToUpdate) {
        userToUpdate = UserWeb.from(userToUpdate, bcrypt.encode(userToUpdate.password()));
        System.out.println(userToUpdate);
        return userRepo.UpdateUser(userId, userToUpdate);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> RemoveUserWrapper(@PathVariable int userId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Admin);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            boolean p = RemoveUser(userId);
            if (!p) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public boolean RemoveUser(int userId) {
        return userRepo.DeleteUser(userId);
    }

    @GetMapping("/me/subs")
    public ResponseEntity<List<UserSubmission>> GetUserSubmissionsWrapper(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            List<UserSubmission> p = GetUserSubmissions(userId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public List<UserSubmission> GetUserSubmissions(int userId) {
        return userSubs.GetUserSubs(userId);
    }

    @GetMapping("/me/stats")
    public ResponseEntity<UserStats> GetSelfStatsWrapper(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        int userId = auth.DecodeJwt(jwt);
        if (o) {
            UserStats p = GetUserStats(userId);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public UserStats GetUserStats(int userId) {
        return userStats.GetUserStats(userId);
    }
}
