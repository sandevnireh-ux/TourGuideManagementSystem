package controller;

import dao.ReviewDAO;
import exception.InvalidRatingException;
import model.Review;
import util.Validator;

import java.sql.SQLException;
import java.util.List;

public class ReviewController {

    private ReviewDAO reviewDAO = new ReviewDAO();

    public boolean addReview(int assignmentId, String ratingStr,
                             String comment) throws SQLException, InvalidRatingException {

        if (Validator.isNullOrEmpty(ratingStr)) throw new IllegalArgumentException("Rating is required.");

        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Rating must be a number.");
        }

        if (!Validator.isValidRating(rating)) throw new InvalidRatingException(rating);

        Review review = new Review();
        review.setAssignmentId(assignmentId);
        review.setRating(rating);
        review.setComment(comment != null ? comment.trim() : "");

        return reviewDAO.addReview(review);
    }

    public boolean deleteReview(int reviewId) throws SQLException {
        return reviewDAO.deleteReview(reviewId);
    }

    public List<Review> getAllReviews() throws SQLException {
        return reviewDAO.getAllReviews();
    }

    public double getAverageRatingForGuide(int guideId) throws SQLException {
        return reviewDAO.getAverageRatingForGuide(guideId);
    }

    public double getOverallAverageRating() throws SQLException {
        return reviewDAO.getOverallAverageRating();
    }

    public int getTotalReviews() throws SQLException {
        return reviewDAO.getTotalReviews();
    }
}