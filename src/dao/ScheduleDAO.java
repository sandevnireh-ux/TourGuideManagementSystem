package dao;

import model.Schedule;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    // CREATE
    public boolean addSchedule(Schedule schedule) throws SQLException {
        String sql = "INSERT INTO schedules (guide_id, tour_name, location_name, latitude, longitude, tour_date, start_time, end_time, max_tourists, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, schedule.getGuideId());
            ps.setString(2, schedule.getTourName());
            ps.setString(3, schedule.getLocationName());
            ps.setDouble(4, schedule.getLatitude());
            ps.setDouble(5, schedule.getLongitude());
            ps.setDate(6, schedule.getTourDate());
            ps.setTime(7, schedule.getStartTime());
            ps.setTime(8, schedule.getEndTime());
            ps.setInt(9, schedule.getMaxTourists());
            ps.setString(10, schedule.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    // READ ALL
    public List<Schedule> getAllSchedules() throws SQLException {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT s.*, g.full_name as guide_name FROM schedules s " +
                "JOIN guides g ON s.guide_id = g.guide_id ORDER BY s.tour_date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapSchedule(rs));
        }
        return list;
    }

    // READ BY ID
    public Schedule getScheduleById(int id) throws SQLException {
        String sql = "SELECT s.*, g.full_name as guide_name FROM schedules s " +
                "JOIN guides g ON s.guide_id = g.guide_id WHERE s.schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapSchedule(rs);
        }
        return null;
    }

    // READ UPCOMING
    public List<Schedule> getUpcomingSchedules() throws SQLException {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT s.*, g.full_name as guide_name FROM schedules s " +
                "JOIN guides g ON s.guide_id = g.guide_id " +
                "WHERE s.status = 'Upcoming' ORDER BY s.tour_date ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapSchedule(rs));
        }
        return list;
    }

    // CHECK GUIDE AVAILABILITY
    public boolean isGuideAvailable(int guideId, Date tourDate, Time startTime, Time endTime) throws SQLException {
        String sql = "SELECT COUNT(*) FROM schedules WHERE guide_id = ? AND tour_date = ? " +
                "AND status != 'Cancelled' AND NOT (end_time <= ? OR start_time >= ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, guideId);
            ps.setDate(2, tourDate);
            ps.setTime(3, startTime);
            ps.setTime(4, endTime);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) == 0;
        }
        return false;
    }

    // GET TOURIST COUNT FOR SCHEDULE
    public int getTouristCount(int scheduleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM assignments WHERE schedule_id = ? AND status = 'Confirmed'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // UPDATE
    public boolean updateSchedule(Schedule schedule) throws SQLException {
        String sql = "UPDATE schedules SET guide_id=?, tour_name=?, location_name=?, latitude=?, " +
                "longitude=?, tour_date=?, start_time=?, end_time=?, max_tourists=?, status=? " +
                "WHERE schedule_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, schedule.getGuideId());
            ps.setString(2, schedule.getTourName());
            ps.setString(3, schedule.getLocationName());
            ps.setDouble(4, schedule.getLatitude());
            ps.setDouble(5, schedule.getLongitude());
            ps.setDate(6, schedule.getTourDate());
            ps.setTime(7, schedule.getStartTime());
            ps.setTime(8, schedule.getEndTime());
            ps.setInt(9, schedule.getMaxTourists());
            ps.setString(10, schedule.getStatus());
            ps.setInt(11, schedule.getScheduleId());
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deleteSchedule(int id) throws SQLException {
        String sql = "DELETE FROM schedules WHERE schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // DASHBOARD STATS
    public int getTotalSchedules() throws SQLException {
        String sql = "SELECT COUNT(*) FROM schedules";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // HELPER
    private Schedule mapSchedule(ResultSet rs) throws SQLException {
        return new Schedule(
                rs.getInt("schedule_id"),
                rs.getInt("guide_id"),
                rs.getString("guide_name"),
                rs.getString("tour_name"),
                rs.getString("location_name"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getDate("tour_date"),
                rs.getTime("start_time"),
                rs.getTime("end_time"),
                rs.getInt("max_tourists"),
                rs.getString("status")
        );
    }
}
