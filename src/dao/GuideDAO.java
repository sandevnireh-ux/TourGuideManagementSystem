package dao;

import model.Guide;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuideDAO {

    public boolean addGuide(Guide guide) throws SQLException {
        String sql = "INSERT INTO guides (full_name, email, phone, language, experience_years, status, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guide.getFullName());
            ps.setString(2, guide.getEmail());
            ps.setString(3, guide.getPhone());
            ps.setString(4, guide.getLanguage());
            ps.setInt(5, guide.getExperienceYears());
            ps.setString(6, guide.getStatus());
            ps.setDouble(7, guide.getLatitude());
            ps.setDouble(8, guide.getLongitude());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Guide> getAllGuides() throws SQLException {
        List<Guide> list = new ArrayList<>();
        String sql = "SELECT * FROM guides";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapGuide(rs));
        }
        return list;
    }

    public Guide getGuideById(int id) throws SQLException {
        String sql = "SELECT * FROM guides WHERE guide_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapGuide(rs);
        }
        return null;
    }

    public List<Guide> getAvailableGuides() throws SQLException {
        List<Guide> list = new ArrayList<>();
        String sql = "SELECT * FROM guides WHERE status = 'Available'";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapGuide(rs));
        }
        return list;
    }

    public boolean updateGuide(Guide guide) throws SQLException {
        String sql = "UPDATE guides SET full_name=?, email=?, phone=?, language=?, experience_years=?, status=?, latitude=?, longitude=? WHERE guide_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guide.getFullName());
            ps.setString(2, guide.getEmail());
            ps.setString(3, guide.getPhone());
            ps.setString(4, guide.getLanguage());
            ps.setInt(5, guide.getExperienceYears());
            ps.setString(6, guide.getStatus());
            ps.setDouble(7, guide.getLatitude());
            ps.setDouble(8, guide.getLongitude());
            ps.setInt(9, guide.getGuideId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteGuide(int id) throws SQLException {
        String sql = "DELETE FROM guides WHERE guide_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateGuideLocation(int guideId, double lat, double lng) throws SQLException {
        String sql = "UPDATE guides SET latitude=?, longitude=? WHERE guide_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, lat);
            ps.setDouble(2, lng);
            ps.setInt(3, guideId);
            return ps.executeUpdate() > 0;
        }
    }

    public int getTotalGuides() throws SQLException {
        String sql = "SELECT COUNT(*) FROM guides";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getAvailableGuidesCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM guides WHERE status = 'Available'";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Guide mapGuide(ResultSet rs) throws SQLException {
        return new Guide(
                rs.getInt("guide_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("language"),
                rs.getInt("experience_years"),
                rs.getString("status"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude")
        );
    }
}