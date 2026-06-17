package pl.krasmap.interaction.infrastructure.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.krasmap.interaction.application.domain.data.review.Review;
import pl.krasmap.interaction.application.domain.data.review.ReviewWeb;
import pl.krasmap.interaction.application.port.out.ReviewFetchInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewFetchPostgres implements ReviewFetchInterface {

    @Value("${db.url}")
    private String postgresString;

    @Value("${db.user}")
    private String postgresUser;

    @Value("${db.password}")
    private String postgresPassword;

    private Connection GetDatabaseConnection() throws Exception {
        return DriverManager.getConnection(postgresString, postgresUser, postgresPassword);
    }

    private Review ReviewFromStatement(ResultSet statement) throws Exception {
        int id, krasnalId, userId;
        short rating;
        String content;
        OffsetDateTime created;
        id = statement.getInt(1);
        krasnalId = statement.getInt(2);
        userId = statement.getInt(3);
        rating = statement.getShort(4);
        content = statement.getString(5);
        created = statement.getObject(6, OffsetDateTime.class);
        return Review.newObject(id,krasnalId, userId, rating, content, created);
    }


    @Override
    public Review GetReview(int reviewId) {
        Review obj = null;
        try{
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery("SELECT * FROM interaction.reviews WHERE id = '" + reviewId + "';");
            while (output.next()){
                obj = ReviewFromStatement(output);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return obj;
    }

    @Override
    public List<Review> GetReviewsUnderKrasnal(int krasnalId) {
        List<Review> list = null;
        try{
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery("SELECT * FROM interaction.reviews WHERE krasnal_id = '" + krasnalId + "';");
            list = new ArrayList<>();
            while (output.next()){
                list.add(ReviewFromStatement(output));
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return list;
    }

    @Override
    public List<Review> GetReviewsFromUser(int userId) {
        List<Review> list = null;
        try{
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery("SELECT * FROM interaction.reviews WHERE author_user_id = '" + userId + "';");
            list = new ArrayList<>();
            while (output.next()){
                list.add(ReviewFromStatement(output));
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return list;
    }

    @Override
    public List<Review> GetReviewsFromUserUnderKrasnal(int userId, int krasnalId) {
        List<Review> list = null;
        try{
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            var output = stat.executeQuery("SELECT * FROM interaction.reviews WHERE author_user_id = '" + userId +
                    "' AND krasnal_id = '" + krasnalId + "';");
            list = new ArrayList<>();
            while (output.next()){
                list.add(ReviewFromStatement(output));
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return list;
    }

    @Override
    public int AddReview(ReviewWeb reviewToAdd) {
        int id = -1;
        String sql = "INSERT INTO interaction.reviews (krasnal_id, author_user_id, rating, content) VALUES " +
                String.format("('%s', ", reviewToAdd.krasnalId()) +
                String.format("'%s', ", reviewToAdd.userId()) +
                String.format("'%s', ", reviewToAdd.rating()) +
                String.format("'%s') RETURNING interaction.reviews.id;", reviewToAdd.content());
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
    public int UpdateReview(int reviewId, ReviewWeb reviewToUpdate) {
        String sql = "UPDATE interaction.reviews SET (krasnal_id, author_user_id, rating, content) = " +
                String.format("('%s', ", reviewToUpdate.krasnalId()) +
                String.format("'%s', ", reviewToUpdate.userId()) +
                String.format("'%s', ", reviewToUpdate.rating()) +
                String.format("'%s')", reviewToUpdate.content()) + " WHERE id = '" + reviewId + "';";
        try {
            Connection conn = GetDatabaseConnection();
            Statement stat = conn.createStatement();
            stat.execute(sql);
            conn.close();
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        return reviewId;
    }

    @Override
    public boolean RemoveReview(int reviewId) {
        boolean check = false;
        String sql = "DELETE FROM interaction.reviews WHERE id = " + reviewId + ";";
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
}
