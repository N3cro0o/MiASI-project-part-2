package pl.krasmap.interaction.infrastructure.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.krasmap.interaction.application.domain.data.fav.NewVisit;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;
import pl.krasmap.interaction.application.port.out.VisitedFetchInterface;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class VisitedFetchPostgres implements VisitedFetchInterface {

    @Value("${db.url}")
    private String postgresString;

    @Value("${db.user}")
    private String postgresUser;

    @Value("${db.password}")
    private String postgresPassword;

    private Connection GetDatabaseConnection() throws Exception {
        return DriverManager.getConnection(postgresString, postgresUser, postgresPassword);
    }

    private VisitedKrasnal VisitFromStatement(ResultSet statement) throws Exception {
        int id, krasnalId, userId;
        OffsetDateTime time;
        id = statement.getInt(1);
        krasnalId = statement.getInt(2);
        userId = statement.getInt(3);
        time = statement.getObject(4, OffsetDateTime.class);
        return new VisitedKrasnal(id, krasnalId, userId, time);
    }

    @Override
    public VisitedKrasnal GetVisit(int visitedId) {
        VisitedKrasnal obj = null;
        String sql = "SELECT * FROM interaction.visited_entries WHERE id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, visitedId);
            var output = stat.executeQuery();
            while(output.next()){
                obj = VisitFromStatement(output);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return obj;
    }

    @Override
    public VisitedKrasnal GetVisit(int userId, int krasnalId) {
        VisitedKrasnal obj = null;
        String sql = "SELECT * FROM interaction.visited_entries WHERE krasnal_id = ? AND user_id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, krasnalId);
            stat.setInt(2, userId);
            var output = stat.executeQuery();
            while(output.next()){
                obj = VisitFromStatement(output);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return obj;
    }

    @Override
    public List<VisitedKrasnal> GetVisitsFromKrasnal(int krasnalId) {
        List<VisitedKrasnal> list = new ArrayList<>();
        String sql = "SELECT * FROM interaction.visited_entries WHERE krasnal_id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, krasnalId);
            var output = stat.executeQuery();
            while(output.next()){
                list.add(VisitFromStatement(output));
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return list;
    }

    @Override
    public List<VisitedKrasnal> GetVisitsFromUser(int userId) {
        List<VisitedKrasnal> list = new ArrayList<>();
        String sql = "SELECT * FROM interaction.visited_entries WHERE user_id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, userId);
            var output = stat.executeQuery();
            while(output.next()){
                list.add(VisitFromStatement(output));
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return list;
    }

    @Override
    public int AddVisit(NewVisit visit) {
        int id = -1;
        String sql = "INSERT INTO interaction.visited_entries (krasnal_id, user_id, visited_at) VALUES (?, ?, ?) " +
                "RETURNING id;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, visit.krasnalId());
            stat.setInt(2, visit.userId());
            stat.setObject(3, OffsetDateTime.now());
            var output = stat.executeQuery();
            while(output.next()){
                id = output.getInt(1);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return id;
    }

    @Override
    public boolean RemoveVisit(int visitedId) {
        boolean check = false;
        String sql = "DELETE FROM interaction.visited_entries WHERE id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, visitedId);
            stat.execute();
            check = true;
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return check;
    }

    @Override
    public boolean RemoveVisit(int userId, int krasnalId) {
        boolean check = false;
        String sql = "DELETE FROM interaction.visited_entries WHERE krasnal_id = ? AND user_id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, krasnalId);
            stat.setInt(2, userId);
            stat.execute();
            check = true;
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return check;
    }
}
