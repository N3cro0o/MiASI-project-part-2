package pl.krasmap.debug;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseCheck {
    @Value("${db.url}")
    private String postgresString;

    @Value("${db.user}")
    private String postgresUser;

    @Value("${db.password}")
    private String postgresPassword;

    private Connection GetDatabaseConnection() throws Exception {
        return DriverManager.getConnection(postgresString, postgresUser, postgresPassword);
    }

    public boolean CheckDBConnection() {
        boolean check = false;
        try {
            Connection connection = GetDatabaseConnection();
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            var output = statement.executeQuery("SELECT * FROM iam.users WHERE id = '1';");
            while (output.next()) {
                System.out.println(String.format("%d %s %s - %s", output.getInt(1), output.getString(2), output.getString(3), output.getString(5)));
            }
            connection.close();
            check = true;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return check;
    }
}
