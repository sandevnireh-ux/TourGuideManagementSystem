package controller;

import dao.TouristDAO;
import model.Tourist;
import util.Validator;

import java.sql.SQLException;
import java.util.List;

public class TouristController {

    private TouristDAO touristDAO = new TouristDAO();

    public boolean addTourist(String fullName, String email, String phone,
                              String nationality, String passportNo) throws SQLException {
        if (Validator.isNullOrEmpty(fullName))   throw new IllegalArgumentException("Full name is required.");
        if (!Validator.isValidEmail(email))       throw new IllegalArgumentException("Invalid email address.");
        if (!Validator.isValidPhone(phone))       throw new IllegalArgumentException("Invalid phone number.");
        if (Validator.isNullOrEmpty(nationality)) throw new IllegalArgumentException("Nationality is required.");
        if (Validator.isNullOrEmpty(passportNo))  throw new IllegalArgumentException("Passport number is required.");

        Tourist tourist = new Tourist();
        tourist.setFullName(fullName.trim());
        tourist.setEmail(email.trim());
        tourist.setPhone(phone.trim());
        tourist.setNationality(nationality.trim());
        tourist.setPassportNo(passportNo.trim());
        return touristDAO.addTourist(tourist);
    }

    public boolean updateTourist(int id, String fullName, String email, String phone,
                                 String nationality, String passportNo) throws SQLException {
        if (Validator.isNullOrEmpty(fullName))   throw new IllegalArgumentException("Full name is required.");
        if (!Validator.isValidEmail(email))       throw new IllegalArgumentException("Invalid email address.");
        if (!Validator.isValidPhone(phone))       throw new IllegalArgumentException("Invalid phone number.");

        Tourist tourist = new Tourist();
        tourist.setTouristId(id);
        tourist.setFullName(fullName.trim());
        tourist.setEmail(email.trim());
        tourist.setPhone(phone.trim());
        tourist.setNationality(nationality.trim());
        tourist.setPassportNo(passportNo.trim());
        return touristDAO.updateTourist(tourist);
    }

    public boolean deleteTourist(int id) throws SQLException {
        return touristDAO.deleteTourist(id);
    }

    public List<Tourist> getAllTourists() throws SQLException {
        return touristDAO.getAllTourists();
    }

    public Tourist getTouristById(int id) throws SQLException {
        return touristDAO.getTouristById(id);
    }

    public int getTotalTourists() throws SQLException {
        return touristDAO.getTotalTourists();
    }
}