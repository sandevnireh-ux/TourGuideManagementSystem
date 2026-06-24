package dao;

import model.Tourist;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TouristDAO {

    public boolean addTourist(Tourist tourist) throws SQLException {
        String sql = "INSERT INTO tourists (full_name, email, phone, nationality, passport_no) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tourist.getFullName());
            ps.setString(2, tourist.getEmail());
            ps.setString(3, tourist.getPhone());
            ps.setString(4, tourist.getNationality());
            ps.setString(5, tourist.getPassportNo());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Tourist> getAllTourists() throws SQLException {
        List<Tourist> list = new ArrayList<>();
        String sql = "SELECT * FROM tourists";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapTourist(rs));
        }
        return list;
    }

    public Tourist getTouristById(int id) throws SQLException {
        String sql = "SELECT * FROM tourists WHERE tourist_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapTourist(rs);
        }
        return null;
    }

    public boolean updateTourist(Tourist tourist) throws SQLException {
        String sql = "UPDATE tourists SET full_name=?, email=?, phone=?, nationality=?, passport_no=? WHERE tourist_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tourist.getFullName());
            ps.setString(2, tourist.getEmail());
            ps.setString(3, tourist.getPhone());
            ps.setString(4, tourist.getNationality());
            ps.setString(5, tourist.getPassportNo());
            ps.setInt(6, tourist.getTouristId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteTourist(int id) throws SQLException {
        String sql = "DELETE FROM tourists WHERE tourist_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int getTotalTourists() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tourists";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Tourist mapTourist(ResultSet rs) throws SQLException {
        return new Tourist(
                rs.getInt("tourist_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("nationality"),
                rs.getString("passport_no")
        );
    }
}