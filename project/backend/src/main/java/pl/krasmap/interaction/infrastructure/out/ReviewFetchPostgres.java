package pl.krasmap.interaction.infrastructure.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.krasmap.interaction.application.domain.data.review.Review;
import pl.krasmap.interaction.application.domain.data.review.ReviewWeb;
import pl.krasmap.interaction.application.port.out.ReviewFetchInterface;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
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
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM interaction.reviews WHERE id = ?;");
            stat.setInt(1, reviewId);
            var output = stat.executeQuery();
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
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM interaction.reviews WHERE krasnal_id = ?;");
            stat.setInt(1, krasnalId);
            var output = stat.executeQuery();
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
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM interaction.reviews WHERE author_user_id = ?;");
            stat.setInt(1, userId);
            var output = stat.executeQuery();
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
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM interaction.reviews WHERE author_user_id = ? " +
                    "AND krasnal_id = ?;");
            stat.setInt(1, userId);
            stat.setInt(2, krasnalId);
            var output = stat.executeQuery();
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
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement("INSERT INTO interaction.reviews (krasnal_id, author_user_id, rating, content) VALUES " +
                    "(?, ?, ?, ?) RETURNING interaction.reviews.id;");
            stat.setInt(1, reviewToAdd.krasnalId());
            stat.setInt(2, reviewToAdd.userId());
            stat.setInt(3, reviewToAdd.rating());
            stat.setString(4, reviewToAdd.content());
            var outcome = stat.executeQuery();
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
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement("UPDATE interaction.reviews SET (krasnal_id, author_user_id, rating, content) = " +
                    "(?, ?, ?, ?) WHERE id = ?;");
            stat.setInt(1, reviewToUpdate.krasnalId());
            stat.setInt(2, reviewToUpdate.userId());
            stat.setInt(3, reviewToUpdate.rating());
            stat.setString(4, reviewToUpdate.content());
            stat.setInt(5, reviewId);
            stat.execute();
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
        try {
            Connection conn = GetDatabaseConnection();
            PreparedStatement stat = conn.prepareStatement("DELETE FROM interaction.reviews WHERE id = ?;");
            stat.setInt(1, reviewId);
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
