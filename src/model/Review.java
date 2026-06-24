package model;

import java.sql.Timestamp;

public class Review {
    private int reviewId;
    private int assignmentId;
    private String touristName;
    private String tourName;
    private int rating;
    private String comment;
    private Timestamp reviewedAt;

    public Review() {}

    public Review(int reviewId, int assignmentId, String touristName,
                  String tourName, int rating, String comment, Timestamp reviewedAt) {
        this.reviewId = reviewId;
        this.assignmentId = assignmentId;
        this.touristName = touristName;
        this.tourName = tourName;
        this.rating = rating;
        this.comment = comment;
        this.reviewedAt = reviewedAt;
    }

    public int getReviewId()            { return reviewId; }
    public int getAssignmentId()        { return assignmentId; }
    public String getTouristName()      { return touristName; }
    public String getTourName()         { return tourName; }
    public int getRating()              { return rating; }
    public String getComment()          { return comment; }
    public Timestamp getReviewedAt()    { return reviewedAt; }

    public void setReviewId(int reviewId)               { this.reviewId = reviewId; }
    public void setAssignmentId(int assignmentId)       { this.assignmentId = assignmentId; }
    public void setTouristName(String touristName)       { this.touristName = touristName; }
    public void setTourName(String tourName)             { this.tourName = tourName; }
    public void setRating(int rating)                   { this.rating = rating; }
    public void setComment(String comment)               { this.comment = comment; }
    public void setReviewedAt(Timestamp reviewedAt)     { this.reviewedAt = reviewedAt; }

    @Override
    public String toString() { return touristName + " rated " + rating + "⭐"; }
}