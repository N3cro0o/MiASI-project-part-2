package pl.krasmap.iam.infrastructure.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.data.UserWeb;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.iam.application.port.out.UserFetchInterface;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserFetchPostgres implements UserFetchInterface {

    @Value("${db.url}")
    private String postgresString;

    @Value("${db.user}")
    private String postgresUser;

    @Value("${db.password}")
    private String postgresPassword;

    private Connection GetDatabaseConnection() throws Exception {
        // Teraz używasz zmiennych bez słowa localhost w kodzie!
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
            Statement statement = connection.createStatement();
            var output = statement.executeQuery(String.format("SELECT * FROM iam.users WHERE iam.users.id = %d;", userId));
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
        String sql = "SELECT * FROM iam.users WHERE login = '" + login + "' OR email = '" + login + "';";
        try {
            Connection connection = GetDatabaseConnection();
            Statement statement = connection.createStatement();
            var output = statement.executeQuery(sql);
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
            Statement statement = connection.createStatement();
            var output = statement.executeQuery("SELECT * FROM iam.users");
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
        String sql = "INSERT INTO iam.users (login, email, hashed_password, role, active) VALUES (" +
                String.format("'%s', ", user.login()) +
                String.format("'%s', ", user.email()) +
                String.format("'%s', ", user.password()) +
                String.format("'%s', ", user.role().toString()) +
                String.format("'%s') RETURNING iam.users.id;", user.active());
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var outcome = stat.executeQuery(sql);
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
        String sql = "UPDATE iam.users SET (login, email, hashed_password, role, active) = (" +
                String.format("'%s', ", user.login()) +
                String.format("'%s', ", user.email()) +
                String.format("'%s', ", user.password()) +
                String.format("'%s', ", user.role().toString()) +
                String.format("'%s') WHERE id = ", user.active()) + userId + ";";

        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            stat.execute(sql);
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        return userId;
    }

    @Override
    public boolean HideUser(int userId) {
        boolean check = false;
        String sql = "UPDATE iam.users SET active = 'f' WHERE id = " + userId + ";";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            stat.execute(sql);
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
        return false;
    }

    @Override
    public String CheckUserPassword(String login) {
        String check = "";
        String sql = "SELECT hashed_password FROM iam.users WHERE login = '" + login + "' OR email = '" + login + "';";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery(sql);
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
