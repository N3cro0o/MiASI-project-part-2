package pl.krasmap.iam.infrastructure.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.port.out.UserFetchInterface;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserFetchPostgres implements UserFetchInterface {

    @Value("${db.url}")
    private String postgresString;

    @Value("${db.user}")
    private String postgresUser;

    @Value("${db.password}")
    private String postgresPassword;

    private Connection GetDatabaseConnection() throws Exception {
        return DriverManager.getConnection(postgresString, postgresUser, postgresPassword);
    }

    @Override
    public void CheckDBConnection() {
        try {
            Connection connection = GetDatabaseConnection();
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            var output = statement.executeQuery("SELECT * FROM iam.users;");
            while (output.next()) {
                System.out.println(String.format("%d %s %s - %s", output.getInt(1), output.getString(2), output.getString(3), output.getString(5)));
            }
            connection.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User GetUserFromStatement(ResultSet statement) throws Exception {
        int id = statement.getInt(1);
        String login = statement.getString(2);
        String email = statement.getString(3);
        UserRole role = UserRole.FromString(statement.getString(5));
        boolean active = statement.getBoolean(6);
        OffsetDateTime created = statement.getObject(7, OffsetDateTime.class);
        return User.newObject(id, login, email, role, active, created);
    }

    @Override
    public User GetUser(int userId) {
        User user = null;
        try {
            Connection connection = GetDatabaseConnection();
            PreparedStatement stat = connection.prepareStatement("SELECT * FROM iam.users WHERE iam.users.id = ?;");
            stat.setInt(1, userId);
            var output = stat.executeQuery();
            while (output.next()) {
                user = GetUserFromStatement(output);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return user;
    }

    @Override
    public User GetUser(String login) {
        User user = null;
        try {
            Connection connection = GetDatabaseConnection();
            PreparedStatement stat = connection.prepareStatement("SELECT * FROM iam.users WHERE login = ? OR email = ?;");
            stat.setString(1, login);
            stat.setString(2, login);
            var output = stat.executeQuery();
            while (output.next()) {
                user = GetUserFromStatement(output);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return user;
    }

    @Override
    public List<User> GetAllUsers() {
        List<User> list = null;
        try {
            Connection connection = GetDatabaseConnection();
            PreparedStatement stat = connection.prepareStatement("SELECT * FROM iam.users");
            var output = stat.executeQuery();
            list = new ArrayList<User>();
            while (output.next()) {
                list.add(GetUserFromStatement(output));
            }
            connection.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return list;
    }

    @Override
    public int SaveUser(UserWeb user) {
        int id = -1;
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement("INSERT INTO iam.users (login, email, hashed_password, role, active)" +
                    " VALUES (?, ?, ?, ?, ?) RETURNING iam.users.id;");
            stat.setString(1, user.login());
            stat.setString(2, user.email());
            stat.setString(3, user.password());
            stat.setString(4, user.role().toString());
            stat.setBoolean(5, user.active());
            var outcome = stat.executeQuery();
            while(outcome.next()) {
                id = outcome.getInt(1);
            }
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        return id;
    }

    @Override
    public int UpdateUser(int userId, UserWeb user) {
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement("UPDATE iam.users SET (login, email, hashed_password, role, active) = " +
                    "(?, ?, ?, ?, ?) WHERE id = ?;");
            stat.setString(1, user.login());
            stat.setString(2, user.email());
            stat.setString(3, user.password());
            stat.setString(4, user.role().toString());
            stat.setBoolean(5, user.active());
            stat.setInt(6, userId);
            stat.execute();
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
            userId = -1;
        }
        return userId;
    }

    @Override
    public boolean HideUser(int userId) {
        boolean check = false;
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement("UPDATE iam.users SET active = 'f' WHERE id = ?;");
            stat.setInt(1, userId);
            stat.execute();
            check = true;
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        return check;
    }

    @Override
    public boolean DeleteUser(int userId) {
        boolean check = false;
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement("DELETE FROM iam.users WHERE id = ?;");
            stat.setInt(1, userId);
            stat.execute();
            check = true;
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        return check;
    }

    @Override
    public String CheckUserPassword(String login) {
        String check = "";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement("SELECT hashed_password FROM iam.users WHERE login = ? OR email = ?;");
            stat.setString(1, login);
            stat.setString(2, login);
            var output = stat.executeQuery();
            while(output.next())
                check = output.getString(1);
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        return check;
    }
}
