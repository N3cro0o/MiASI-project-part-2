package pl.krasmap.submission.infrastructure.out;

import io.swagger.v3.core.util.Json;
import org.springframework.stereotype.Service;
import pl.krasmap.submission.application.domain.NewSubmission;
import pl.krasmap.submission.application.domain.submission.Submission;
import pl.krasmap.submission.application.domain.submission.SubmissionReview;
import pl.krasmap.submission.application.domain.submission.SubmissionStatus;
import pl.krasmap.submission.application.port.out.SubmissionFetchInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionFetchPostgres implements SubmissionFetchInterface {
    private final String postgresAddr = "172.30.144.1:5432";
    private final String postgresString = "jdbc:postgresql://%s/krasnal_db";
    private final String postgresUser = "krasnal_admin";
    private final String postgresPassword = "krasnal";

    private Connection GetDatabaseConnection() throws Exception {
        String targetString = String.format(postgresString, postgresAddr);
        return DriverManager.getConnection(targetString, postgresUser, postgresPassword);
    }

    private String GetJsonFromSub(NewSubmission sub) {
        return Json.pretty(sub.data());
    }

    private Submission GetSubFromStatement(ResultSet statement) throws Exception {
        int id, userId;
        String json;
        String status;
        int reviewerId;
        String rejectionReason;
        OffsetDateTime submittedTime, reviewTime;
        id = statement.getInt(1);
        userId = statement.getInt(2);
        json = statement.getString(3);
        status = statement.getString(4);
        submittedTime = statement.getObject(7, OffsetDateTime.class);
        SubmissionStatus st = SubmissionStatus.FromString(status);
        switch (st) {
            case Rejected -> {
                reviewTime = statement.getObject(8, OffsetDateTime.class);
                rejectionReason = statement.getString(5);
                reviewerId = statement.getInt(6);
                SubmissionReview rv = SubmissionReview.newObject(reviewerId, rejectionReason, reviewTime);
                return Submission.newObject(id, userId, json, st, submittedTime, rv);
            }
            case Accepted -> {
                reviewTime = statement.getObject(8, OffsetDateTime.class);
                reviewerId = statement.getInt(6);
                SubmissionReview rv = SubmissionReview.newObject(reviewerId, reviewTime);
                return Submission.newObject(id, userId, json, st, submittedTime, rv);
            }
            default -> {
                return Submission.newObject(id, userId, json, st, submittedTime);
            }
        }
    }

    @Override
    public int AddSubmission(NewSubmission sub) {
        String json = GetJsonFromSub(sub);
        String sql = "INSERT INTO verification.submissions (submitted_by_user_id, payload_json) VALUES (" +
                String.format("'%s', ", sub.userId()) +
                String.format("'%s') RETURNING verification.submissions.id;", json);
        int id = -1;
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var outcome = stat.executeQuery(sql);
            while (outcome.next()){
                id = outcome.getInt(1);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return id;
    }

    @Override
    public Submission GetSubmission(int subId) {
        Submission obj = null;
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var outcome = stat.executeQuery("SELECT * FROM verification.submissions WHERE id = '" + subId + "';");
            while (outcome.next()){
                obj = GetSubFromStatement(outcome);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return obj;
    }

    @Override
    public SubmissionStatus CheckSubmission(int subId) {
        SubmissionStatus obj = null;
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var outcome = stat.executeQuery("SELECT status FROM verification.submissions WHERE id = '" + subId + "';");
            while (outcome.next()){
                obj = SubmissionStatus.FromString(outcome.getString(1));
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return obj;
    }

    @Override
    public List<Submission> GetSubmissionsFromUser(int userId) {
        List<Submission> list = null;
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var outcome = stat.executeQuery("SELECT * FROM verification.submissions WHERE submitted_by_user_id = '" + userId + "';");
            list = new ArrayList<>();
            while (outcome.next()){
                list.add(GetSubFromStatement(outcome));
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return list;
    }
}
