package pl.krasmap.interaction.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.interaction.application.domain.Review;

import java.util.ArrayList;
import java.util.List;

@Service
public class HoldReviewRepo {
    private final List<Review> reviewList = new ArrayList<>();

    public List<Review> getReviewList() {
        return reviewList;
    }

    public List<Review> getReviewList(int krasnalId) {
        List<Review> list = new ArrayList<>();
        for (Review r : reviewList) {
            if (r.krasnalId() == krasnalId) { list.add(r); }
        }
        return list;
    }
}
