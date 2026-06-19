package pl.krasmap.submission.infrastructure.out;

import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.krasmap.submission.application.domain.data.NewSubmission;
import pl.krasmap.submission.application.domain.data.submission.Submission;
import pl.krasmap.submission.application.domain.data.submission.SubmissionReview;
import pl.krasmap.common.data.SubmissionStatus;
import pl.krasmap.submission.application.port.out.SubmissionFetchInterface;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
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
        String sql = "INSERT INTO verification.submissions (submitted_by_user_id, payload_json) VALUES (?, ?::jsonb) RETURNING verification.submissions.id;";
        int id = -1;
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, sub.userId());
            stat.setString(2, json);
            var outcome = stat.executeQuery();
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
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM verification.submissions WHERE id = ?;");
            stat.setInt(1, subId);
            var outcome = stat.executeQuery();
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
            PreparedStatement stat = conn.prepareStatement("SELECT status FROM verification.submissions WHERE id = ?;");
            stat.setInt(1, subId);
            var outcome = stat.executeQuery();
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
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM verification.submissions WHERE submitted_by_user_id = ?;");
            stat.setInt(1, userId);
            var outcome = stat.executeQuery();
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
        String sql = "UPDATE verification.submissions SET (status, rejection_reason, reviewed_by_user_id, reviewed_at) = (?, ?, ?, ?) " +
                "WHERE id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, newSub.status().toString());
            stat.setString(2, newSub.review().rejectionReason());
            stat.setInt(3, newSub.review().reviewerId());
            stat.setObject(4, newSub.review().reviewTime());
            stat.setInt(5, newSub.id());
            stat.execute();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return check;
    }

    @Override
    public int UpdateSubmission(int subId, NewSubmission submission) {
        String sql = "UPDATE verification.submissions SET (submitted_by_user_id, payload_json) = (?, ?::jsonb) WHERE id = ?;";
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, submission.userId());
            stat.setString(2, GetJsonFromSub(submission));
            stat.setInt(3, subId);
            stat.execute();
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
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM verification.submissions;");
            var outcome = stat.executeQuery();
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
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM verification.submissions WHERE status = ?::verification.submission_status;");
            stat.setString(1, status.toString());
            var outcome = stat.executeQuery();
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
