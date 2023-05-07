package edu.bluejack22_2.agocar.other;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.models.UserReview;

public interface RetrievedUserReviewsListener {
    void retrievedUserReviews(ArrayList<UserReview> userReviews);
}
