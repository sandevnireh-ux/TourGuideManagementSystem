package controller;

import dao.GuideDAO;
import exception.GuideNotAvailableException;
import model.Guide;
import util.Validator;

import java.sql.SQLException;
import java.util.List;

public class GuideController {

    private GuideDAO guideDAO = new GuideDAO();

    public boolean addGuide(String fullName, String email, String phone,
                            String language, String experienceYears,
                            String status, double lat, double lng) throws SQLException, GuideNotAvailableException {
        if (Validator.isNullOrEmpty(fullName)) throw new IllegalArgumentException("Full name is required.");
        if (!Validator.isValidEmail(email)) throw new IllegalArgumentException("Invalid email address.");
        if (!Validator.isValidPhone(phone)) throw new IllegalArgumentException("Invalid phone number.");
        if (Validator.isNullOrEmpty(language)) throw new IllegalArgumentException("Language is required.");
        if (!Validator.isPositiveInt(experienceYears)) throw new IllegalArgumentException("Experience must be a positive number.");

        Guide guide = new Guide();
        guide.setFullName(fullName.trim());
        guide.setEmail(email.trim());
        guide.setPhone(phone.trim());
        guide.setLanguage(language.trim());
        guide.setExperienceYears(Integer.parseInt(experienceYears));
        guide.setStatus(status);
        guide.setLatitude(lat);
        guide.setLongitude(lng);

        return guideDAO.addGuide(guide);
    }

    public boolean updateGuide(int guideId, String fullName, String email, String phone,
                               String language, String experienceYears,
                               String status, double lat, double lng) throws SQLException {
        if (Validator.isNullOrEmpty(fullName)) throw new IllegalArgumentException("Full name is required.");
        if (!Validator.isValidEmail(email)) throw new IllegalArgumentException("Invalid email address.");
        if (!Validator.isValidPhone(phone)) throw new IllegalArgumentException("Invalid phone number.");
        if (!Validator.isPositiveInt(experienceYears)) throw new IllegalArgumentException("Experience must be a positive number.");

        Guide guide = new Guide();
        guide.setGuideId(guideId);
        guide.setFullName(fullName.trim());
        guide.setEmail(email.trim());
        guide.setPhone(phone.trim());
        guide.setLanguage(language.trim());
        guide.setExperienceYears(Integer.parseInt(experienceYears));
        guide.setStatus(status);
        guide.setLatitude(lat);
        guide.setLongitude(lng);

        return guideDAO.updateGuide(guide);
    }

    public boolean deleteGuide(int guideId) throws SQLException {
        return guideDAO.deleteGuide(guideId);
    }

    public List<Guide> getAllGuides() throws SQLException {
        return guideDAO.getAllGuides();
    }

    public List<Guide> getAvailableGuides() throws SQLException {
        return guideDAO.getAvailableGuides();
    }

    public Guide getGuideById(int id) throws SQLException {
        return guideDAO.getGuideById(id);
    }

    public boolean updateGuideLocation(int guideId, double lat, double lng) throws SQLException {
        return guideDAO.updateGuideLocation(guideId, lat, lng);
    }

    public int getTotalGuides() throws SQLException {
        return guideDAO.getTotalGuides();
    }

    public int getAvailableGuidesCount() throws SQLException {
        return guideDAO.getAvailableGuidesCount();
    }
}