package pl.krasmap.interaction.infrastructure.in;

import org.springframework.web.bind.annotation.*;
import pl.krasmap.interaction.application.domain.Review;
import pl.krasmap.interaction.application.domain.ReviewWeb;
import pl.krasmap.interaction.application.port.in.ReviewControllerInterface;
import pl.krasmap.interaction.application.service.HoldReviewRepo;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewControllerWeb implements ReviewControllerInterface {

    private final HoldReviewRepo reviewRepo;

    public ReviewControllerWeb(HoldReviewRepo repo){
        reviewRepo = repo;
    }

    @Override
    @GetMapping("/get/{reviewId}")
    public Review GetReview(@PathVariable int reviewId) {
        return reviewRepo.GetReviewById(reviewId);
    }

    @Override
    @GetMapping("/get/krasnal/{krasnalId}")
    public List<Review> GetReviewsUnderKrasnal(@PathVariable int krasnalId) {
        return reviewRepo.GetReviewsUnderKrasnal(krasnalId);
    }

    @Override
    @GetMapping("/get/user/{userId}")
    public List<Review> GetReviewsFromUser(@PathVariable int userId) {
        return reviewRepo.GetReviewsFromUser(userId);
    }

    @Override
    @GetMapping("/get/user/{userId}/krasnal/{krasnalId}")
    public List<Review> GetReviewsFromUserUnderKrasnal(@PathVariable int userId,@PathVariable int krasnalId) {
        return reviewRepo.GetReviewsFromUserUnderKrasnal(userId, krasnalId);
    }

    @Override
    @PostMapping("/add")
    public Review AddReview(@RequestBody ReviewWeb reviewToAdd) {
        return reviewRepo.AddReview(reviewToAdd);
    }

    @Override
    @PatchMapping("/update/{reviewId}")
    public Review UpdateReview(@PathVariable int reviewId, @RequestBody ReviewWeb reviewToUpdate) {
        return reviewRepo.UpdateReview(reviewId, reviewToUpdate);
    }

    @Override
    @DeleteMapping("/delete/{reviewId}")
    public boolean RemoveReview(@PathVariable int reviewId) {
        return reviewRepo.RemoveReview(reviewId);
    }
}
