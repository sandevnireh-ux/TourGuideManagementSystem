package dao;

import model.Assignment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {

    // CREATE
    public boolean addAssignment(Assignment assignment) throws SQLException {
        String sql = "INSERT INTO assignments (schedule_id, tourist_id, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignment.getScheduleId());
            ps.setInt(2, assignment.getTouristId());
            ps.setString(3, assignment.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    // READ ALL
    public List<Assignment> getAllAssignments() throws SQLException {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.*, t.full_name as tourist_name, s.tour_name " +
                "FROM assignments a " +
                "JOIN tourists t ON a.tourist_id = t.tourist_id " +
                "JOIN schedules s ON a.schedule_id = s.schedule_id " +
                "ORDER BY a.assigned_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapAssignment(rs));
        }
        return list;
    }

    // READ BY SCHEDULE
    public List<Assignment> getAssignmentsBySchedule(int scheduleId) throws SQLException {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.*, t.full_name as tourist_name, s.tour_name " +
                "FROM assignments a " +
                "JOIN tourists t ON a.tourist_id = t.tourist_id " +
                "JOIN schedules s ON a.schedule_id = s.schedule_id " +
                "WHERE a.schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapAssignment(rs));
        }
        return list;
    }

    // CHECK IF TOURIST ALREADY ASSIGNED
    public boolean isTouristAssigned(int scheduleId, int touristId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM assignments WHERE schedule_id = ? AND tourist_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            ps.setInt(2, touristId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    // UPDATE STATUS
    public boolean updateAssignmentStatus(int assignmentId, String status) throws SQLException {
        String sql = "UPDATE assignments SET status = ? WHERE assignment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, assignmentId);
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deleteAssignment(int id) throws SQLException {
        String sql = "DELETE FROM assignments WHERE assignment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // GET COMPLETED ASSIGNMENTS WITHOUT REVIEW
    public List<Assignment> getCompletedWithoutReview() throws SQLException {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.*, t.full_name as tourist_name, s.tour_name " +
                "FROM assignments a " +
                "JOIN tourists t ON a.tourist_id = t.tourist_id " +
                "JOIN schedules s ON a.schedule_id = s.schedule_id " +
                "LEFT JOIN reviews r ON a.assignment_id = r.assignment_id " +
                "WHERE a.status = 'Completed' AND r.review_id IS NULL";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapAssignment(rs));
        }
        return list;
    }

    // DASHBOARD STATS
    public int getTotalAssignments() throws SQLException {
        String sql = "SELECT COUNT(*) FROM assignments";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // HELPER
    private Assignment mapAssignment(ResultSet rs) throws SQLException {
        return new Assignment(
                rs.getInt("assignment_id"),
                rs.getInt("schedule_id"),
                rs.getInt("tourist_id"),
                rs.getString("tourist_name"),
                rs.getString("tour_name"),
                rs.getTimestamp("assigned_at"),
                rs.getString("status")
        );
    }
}