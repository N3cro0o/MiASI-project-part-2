package pl.krasmap.interaction.infrastructure.in;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.krasmap.common.auth.template.UserAuthInterface;
import pl.krasmap.common.data.UserRole;
import pl.krasmap.interaction.application.domain.review.Review;
import pl.krasmap.interaction.application.domain.review.ReviewWeb;
import pl.krasmap.interaction.application.port.in.ReviewControllerInterface;
import pl.krasmap.interaction.application.service.HoldReviewRepo;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewControllerWeb implements ReviewControllerInterface {

    private final HoldReviewRepo reviewRepo;
    private final UserAuthInterface auth;

    public ReviewControllerWeb(HoldReviewRepo repo, @Lazy UserAuthInterface authServ){
        reviewRepo = repo;
        auth = authServ;
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> GetReviewWrapper(@PathVariable int reviewId) {
        Review p = GetReview(reviewId);
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public Review GetReview(int reviewId) {
        return reviewRepo.GetReviewById(reviewId);
    }

    @GetMapping("/krasnal/{krasnalId}")
    public ResponseEntity<List<Review>> GetReviewsUnderKrasnalWrapper(@PathVariable int krasnalId) {
        List<Review> p = GetReviewsUnderKrasnal(krasnalId);
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public List<Review> GetReviewsUnderKrasnal(int krasnalId) {
        return reviewRepo.GetReviewsUnderKrasnal(krasnalId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> GetReviewsFromUserWrapper(@PathVariable int userId) {
        List<Review> p = GetReviewsFromUser(userId);
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public List<Review> GetReviewsFromUser(int userId) {
        return reviewRepo.GetReviewsFromUser(userId);
    }

    @GetMapping("/user/{userId}/krasnal/{krasnalId}")
    public ResponseEntity<List<Review>> GetReviewsFromUserUnderKrasnalWrapper(@PathVariable int userId, @PathVariable int krasnalId) {
        List<Review> p = GetReviewsFromUserUnderKrasnal(userId, krasnalId);
        if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(p, HttpStatus.valueOf(200));
    }

    @Override
    public List<Review> GetReviewsFromUserUnderKrasnal(int userId, int krasnalId) {
        return reviewRepo.GetReviewsFromUserUnderKrasnal(userId, krasnalId);
    }

    @PostMapping
    public ResponseEntity<Review> AddReviewWrapper(@RequestBody ReviewWeb reviewToAdd, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Review p = AddReview(reviewToAdd);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Review AddReview(ReviewWeb reviewToAdd) {
        return reviewRepo.AddReview(reviewToAdd);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Review> UpdateReviewWrapper(@PathVariable int reviewId, @RequestBody ReviewWeb reviewToUpdate, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Review p = UpdateReview(reviewId, reviewToUpdate);
            if (p == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Review UpdateReview(int reviewId, ReviewWeb reviewToUpdate) {
        return reviewRepo.UpdateReview(reviewId, reviewToUpdate);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Boolean> RemoveReviewWrapper(@PathVariable int reviewId, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        var o = auth.CheckAccess(jwt, UserRole.Wanderer);
        if (o == null) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(500));
        if (o) {
            Boolean p = RemoveReview(reviewId);
            if (p == null || !p) return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(p, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>((HttpHeaders) null, HttpStatus.valueOf(400));
    }

    @Override
    public Boolean RemoveReview(int reviewId) {
        return reviewRepo.RemoveReview(reviewId);
    }
}
