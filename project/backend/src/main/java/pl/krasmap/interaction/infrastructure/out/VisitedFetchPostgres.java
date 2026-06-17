package pl.krasmap.interaction.infrastructure.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.krasmap.interaction.application.domain.data.fav.NewVisit;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;
import pl.krasmap.interaction.application.port.out.VisitedFetchInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
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
        String sql = "SELECT * FROM interaction.visited_entries WHERE id = '" + visitedId + "';";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery(sql);
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
        String sql = "SELECT * FROM interaction.visited_entries WHERE " +
                "krasnal_id = '" + krasnalId + "' AND user_id = '" + userId + "';";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery(sql);
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
        List<VisitedKrasnal> list = null;
        String sql = "SELECT * FROM interaction.visited_entries WHERE krasnal_id = '" + krasnalId + "';";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery(sql);
            list = new ArrayList<>();
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
        List<VisitedKrasnal> list = null;
        String sql = "SELECT * FROM interaction.visited_entries WHERE user_id = '" + userId + "';";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery(sql);
            list = new ArrayList<>();
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
        String sql = "INSERT INTO interaction.visited_entries (krasnal_id, user_id, visited_at) VALUES ("+
                String.format("'%s', ", visit.krasnalId()) +
                String.format("'%s', ", visit.userId()) +
                String.format("'%s') RETURNING id;", OffsetDateTime.now());
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery(sql);
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
        String sql = "DELETE FROM interaction.visited_entries WHERE id = '" + visitedId + "';";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            stat.execute(sql);
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
        String sql = "SELECT * FROM interaction.visited_entries WHERE " +
                "krasnal_id = '" + krasnalId + "' AND user_id = '" + userId + "';";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            stat.execute(sql);
            check = true;
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return check;
    }
}
