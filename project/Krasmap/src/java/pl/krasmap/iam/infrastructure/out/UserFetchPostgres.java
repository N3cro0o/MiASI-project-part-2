package pl.krasmap.iam.infrastructure.out;

import pl.krasmap.iam.application.domain.User;
import pl.krasmap.iam.application.domain.UserRole;
import pl.krasmap.iam.application.port.out.UserFetchInterface;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserFetchPostgres implements UserFetchInterface {

    private final String postgresAddr = "172.30.144.1:5432";
    private final String postgresString = "jdbc:postgresql://%s/krasnal_db";
    private final String postgresUser = "krasnal_admin";
    private final String postgresPassword = "krasnal";

    private Connection getConnection() throws Exception {
        String targetString = String.format(postgresString, postgresAddr);
        return DriverManager.getConnection(targetString, postgresUser, postgresPassword);
    }

    @Override
    public void CheckDBConnection() {
        try {
            String targetString = String.format(postgresString, postgresAddr);
            Connection connection = DriverManager.getConnection(targetString, postgresUser, postgresPassword);
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            var output = statement.executeQuery("SELECT * FROM iam.users;");
            while (output.next()) {
                System.out.println(String.format("%d %s %s - %s", output.getInt(1), output.getString(2), output.getString(3), output.getString(5)));
            }
            connection.close();
        }
        catch (SQLException e) {
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
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            var output = statement.executeQuery(String.format("SELECT * FROM iam.users WHERE iam.users.id = %d;", userId));
            while (output.next()) {
                user = GetUserFromStatement(output);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return user;
    }

    @Override
    public List<User> GetAllUsers() {
        List<User> list = null;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            var output = statement.executeQuery("SELECT * FROM iam.users");
            list = new ArrayList<User>();
            while (output.next()) {
                list.add(GetUserFromStatement(output));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return list;
    }
}
