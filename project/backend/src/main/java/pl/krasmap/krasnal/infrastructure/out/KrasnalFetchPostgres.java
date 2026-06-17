package pl.krasmap.krasnal.infrastructure.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.UpdateTime;
import pl.krasmap.krasnal.application.domain.data.krasnal.*;
import pl.krasmap.krasnal.application.port.out.KrasnalFetchInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class KrasnalFetchPostgres implements KrasnalFetchInterface {

    // private final String postgresAddr = "172.30.144.1:5432";
    // private final String postgresString = "jdbc:postgresql://%s/krasnal_db";
    // private final String postgresUser = "krasnal_admin";
    // private final String postgresPassword = "krasnal";

    // // Use local address for Docker port forwarding
    // private final String postgresAddr = "127.0.0.1:5432";
    
    // // Target database name
    // private final String postgresString = "jdbc:postgresql://%s/krasmap";
    
    // // Credentials from the .env file
    // private final String postgresUser = "krasmap_user";
    // private final String postgresPassword = "change_me_in_production";

    // private Connection GetDatabaseConnection() throws Exception {
    //     String targetString = String.format(postgresString, postgresAddr);
    //     return DriverManager.getConnection(targetString, postgresUser, postgresPassword);
    // }

    @Value("${db.url}")
    private String postgresString;

    @Value("${db.user}")
    private String postgresUser;

    @Value("${db.password}")
    private String postgresPassword;

    private Connection GetDatabaseConnection() throws Exception {
        System.out.printf("%s, %s, %s\n", postgresString, postgresUser, postgresPassword);
        var s = String.format("jdbc:postgresql://%s/krasnal_db", "172.30.144.1:5432");
        return DriverManager.getConnection(postgresString, postgresUser, postgresPassword);
    }

    private Krasnal GetKrasnalFromStatement(ResultSet statement) throws Exception {
        int id = statement.getInt(1);
        String name, desc;
        name = statement.getString(2);
        desc = statement.getString(3);
        float lat, lon;
        lat = statement.getFloat(4);
        lon = statement.getFloat(5);
        KrasnalCategory cat = KrasnalCategory.FromString(statement.getString(6));
        KrasnalStatus stat = KrasnalStatus.FromString(statement.getString(7));
        UpdateTime times;
        OffsetDateTime t1, t2;
        t1 = statement.getObject(8, OffsetDateTime.class);
        t2 = statement.getObject(9, OffsetDateTime.class);
        times = UpdateTime.from(t1, t2);
        return Krasnal.newObject(id, name, desc, Position.from(lat, lon), cat, stat, times);
    }

    @Override
    public List<Krasnal> GetAllKrasnalObjects() {
        List<Krasnal> list = null;
        try {
            Connection connection = GetDatabaseConnection();
            Statement statement = connection.createStatement();
            var output = statement.executeQuery("SELECT * FROM poi_catalog.krasnals;");
            list = new ArrayList<>();
            while (output.next()) {
                list.add(GetKrasnalFromStatement(output));
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Krasnal GetKrasnal(int krasnalId) {
        Krasnal obj = null;
        try {
            Connection conn = GetDatabaseConnection();
            Statement statement = conn.createStatement();
            var output = statement.executeQuery(String.format("SELECT * FROM poi_catalog.krasnals WHERE poi_catalog.krasnals.id = %d;", krasnalId));
            while (output.next()) {
                obj = GetKrasnalFromStatement(output);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return obj;
    }

    @Override
    public int SaveKrasnal(Krasnal k) {
        int id = -1;
        String sql = "INSERT INTO poi_catalog.krasnals (name, description, latitude, longitude, category, status) VALUES (" + String.format("'%s', ", k.name()) +
                String.format("'%s', ", k.description()) +
                String.format("'%s', ", k.position().latitude()) +
                String.format("'%s', ", k.position().longitude()) +
                String.format("'%s', ", k.category().toString()) +
                String.format("'%s'", k.status().toString()) +
                ") RETURNING poi_catalog.krasnals.id;";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery(sql);
            while(output.next()) {
                id = output.getInt(1);
            }
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        return id;
    }

    @Override
    public int UpdateKrasnal(Krasnal k) {
        String sql = "UPDATE poi_catalog.krasnals SET (name, description, latitude, longitude, category, status) = (" + String.format("'%s', ", k.name()) +
                String.format("'%s', ", k.description()) +
                String.format("'%s', ", k.position().latitude()) +
                String.format("'%s', ", k.position().longitude()) +
                String.format("'%s', ", k.category().toString()) +
                String.format("'%s'", k.status().toString()) +
                ") WHERE id = " + k.id() + ";";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            stat.execute(sql);
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        return k.id();
    }

    @Override
    public boolean HideKrasnal(int krasnalId) {
        boolean check = false;
        String sql = "UPDATE poi_catalog.krasnals SET status = '" +
                KrasnalStatus.Inactive.toString() +
                "' WHERE id = " + krasnalId + ";";
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
    public boolean DeleteKrasnal(int krasnalId) {
        return false;
    }
}
