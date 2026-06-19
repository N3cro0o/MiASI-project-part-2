package pl.krasmap.krasnal.infrastructure.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.UpdateTime;
import pl.krasmap.krasnal.application.domain.data.krasnal.*;
import pl.krasmap.krasnal.application.port.out.KrasnalFetchInterface;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class KrasnalFetchPostgres implements KrasnalFetchInterface {

    @Value("${db.url}")
    private String postgresString;

    @Value("${db.user}")
    private String postgresUser;

    @Value("${db.password}")
    private String postgresPassword;

    private Connection GetDatabaseConnection() throws Exception {
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
        Double averageRating = statement.getDouble("averageRating");
        return Krasnal.newObject(id, name, desc, Position.from(lat, lon), cat, stat, times, averageRating);
    }

    @Override
    public List<Krasnal> GetAllKrasnalObjects() {
        List<Krasnal> list = null;
        try {
            Connection connection = GetDatabaseConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT k.*, COALESCE(ROUND(AVG(r.rating), 1), 0.0) as averageRating FROM poi_catalog.krasnals k LEFT JOIN interaction.reviews r ON k.id = r.krasnal_id GROUP BY k.id;");
            var output = statement.executeQuery();
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
            PreparedStatement statement = conn.prepareStatement("SELECT k.*, COALESCE(ROUND(AVG(r.rating), 1), 0.0) as averageRating FROM poi_catalog.krasnals k LEFT JOIN interaction.reviews r ON k.id = r.krasnal_id WHERE k.id = ? GROUP BY k.id;");
            statement.setInt(1, krasnalId);
            var output = statement.executeQuery();
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
        String sql = "INSERT INTO poi_catalog.krasnals (name, description, latitude, longitude, category, status) VALUES (?, ?, ?, ?, ?::poi_catalog.krasnal_category, ?::poi_catalog.krasnal_status)" +
                " RETURNING poi_catalog.krasnals.id;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, k.name());
            stat.setString(2, k.description());
            stat.setDouble(3, k.position().latitude());
            stat.setDouble(4, k.position().longitude());
            stat.setString(5, k.category().toString());
            stat.setString(6, k.status().toString());
            var output = stat.executeQuery();
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
        String sql = "UPDATE poi_catalog.krasnals SET (name, description, latitude, longitude, category, status) = (?, ?, ?, ?, ?::poi_catalog.krasnal_category, ?::poi_catalog.krasnal_status) " +
                "WHERE id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, k.name());
            stat.setString(2, k.description());
            stat.setDouble(3, k.position().latitude());
            stat.setDouble(4, k.position().longitude());
            stat.setString(5, k.category().toString());
            stat.setString(6, k.status().toString());
            stat.setInt(7, k.id());
            stat.execute();
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
        String sql = "UPDATE poi_catalog.krasnals SET status = ?::poi_catalog.krasnal_status WHERE id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, KrasnalStatus.Inactive.toString());
            stat.setInt(2, krasnalId);
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
    public boolean DeleteKrasnal(int krasnalId) {
        boolean check = false;
        String sql = "DELETE FROM poi_catalog.krasnals WHERE id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, krasnalId);
            stat.execute();
            check = true;
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        return check;
    }
}
