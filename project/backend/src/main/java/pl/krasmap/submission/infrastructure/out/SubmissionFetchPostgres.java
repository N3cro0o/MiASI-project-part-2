package pl.krasmap.submission.infrastructure.out;

import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.krasmap.submission.application.domain.data.NewSubmission;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.submission.application.domain.data.submission.SubmissionReview;
import pl.krasmap.common.data.SubmissionStatus;
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
    @Value("${db.url}")
    private String postgresString;

    @Value("${db.user}")
    private String postgresUser;

    @Value("${db.password}")
    private String postgresPassword;

    private Connection GetDatabaseConnection() throws Exception {
        return DriverManager.getConnection(postgresString, postgresUser, postgresPassword);
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

    @Override
    public boolean UpdateSubReview(Submission newSub) {
        boolean check = false;
        String sql = "UPDATE verification.submissions SET (status, rejection_reason, reviewed_by_user_id, reviewed_at) = (" +
                String.format("'%s', ", newSub.status()) +
                String.format("'%s', ", newSub.review().rejectionReason()) +
                String.format("'%s', ", newSub.review().reviewerId()) +
                String.format("'%s')", newSub.review().reviewTime()) +
                "WHERE id = " + newSub.id() + ";";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            stat.execute(sql);
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return check;
    }

    @Override
    public int UpdateSubmission(int subId, NewSubmission submission) {
        String sql = "UPDATE verification.submissions SET (submitted_by_user_id, payload_json) = (" +
                String.format("'%s', ", submission.userId()) +
                String.format("'%s')", GetJsonFromSub(submission)) +
                "WHERE id = " + subId + ";";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            stat.execute(sql);
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return subId;
    }

    @Override
    public List<Submission> GetAllSubmissions() {
        List<Submission> list = null;
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var outcome = stat.executeQuery("SELECT * FROM verification.submissions;");
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

    @Override
    public List<Submission> GetAllSubmissions(SubmissionStatus status) {
        List<Submission> list = null;
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var outcome = stat.executeQuery("SELECT * FROM verification.submissions WHERE status = '" + status.toString() + "';");
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
