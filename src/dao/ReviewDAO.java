package dao;

import model.Review;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    // CREATE
    public boolean addReview(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (assignment_id, rating, comment) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, review.getAssignmentId());
            ps.setInt(2, review.getRating());
            ps.setString(3, review.getComment());
            return ps.executeUpdate() > 0;
        }
    }

    // READ ALL
    public List<Review> getAllReviews() throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.*, t.full_name as tourist_name, s.tour_name " +
                "FROM reviews r " +
                "JOIN assignments a ON r.assignment_id = a.assignment_id " +
                "JOIN tourists t ON a.tourist_id = t.tourist_id " +
                "JOIN schedules s ON a.schedule_id = s.schedule_id " +
                "ORDER BY r.reviewed_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapReview(rs));
        }
        return list;
    }

    // GET AVERAGE RATING FOR A GUIDE
    public double getAverageRatingForGuide(int guideId) throws SQLException {
        String sql = "SELECT AVG(r.rating) FROM reviews r " +
                "JOIN assignments a ON r.assignment_id = a.assignment_id " +
                "JOIN schedules s ON a.schedule_id = s.schedule_id " +
                "WHERE s.guide_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, guideId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    // GET OVERALL AVERAGE RATING
    public double getOverallAverageRating() throws SQLException {
        String sql = "SELECT AVG(rating) FROM reviews";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    // DELETE
    public boolean deleteReview(int id) throws SQLException {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // TOTAL REVIEWS COUNT
    public int getTotalReviews() throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // HELPER
    private Review mapReview(ResultSet rs) throws SQLException {
        return new Review(
                rs.getInt("review_id"),
                rs.getInt("assignment_id"),
                rs.getString("tourist_name"),
                rs.getString("tour_name"),
                rs.getInt("rating"),
                rs.getString("comment"),
                rs.getTimestamp("reviewed_at")
        );
    }
}