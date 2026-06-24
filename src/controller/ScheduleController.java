package controller;

import dao.ScheduleDAO;
import exception.GuideNotAvailableException;
import model.Schedule;
import util.Validator;

import java.sql.*;
import java.util.List;

public class ScheduleController {

    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    public boolean addSchedule(int guideId, String guideName, String tourName,
                               String locationName, double lat, double lng,
                               Date tourDate, Time startTime, Time endTime,
                               String maxTourists) throws SQLException, GuideNotAvailableException {

        if (Validator.isNullOrEmpty(tourName)) throw new IllegalArgumentException("Tour name is required.");
        if (Validator.isNullOrEmpty(locationName)) throw new IllegalArgumentException("Location is required.");
        if (tourDate == null) throw new IllegalArgumentException("Tour date is required.");
        if (startTime == null || endTime == null) throw new IllegalArgumentException("Start and end time are required.");
        if (!Validator.isPositiveInt(maxTourists)) throw new IllegalArgumentException("Max tourists must be a positive number.");

        // Check guide availability
        boolean available = scheduleDAO.isGuideAvailable(guideId, tourDate, startTime, endTime);
        if (!available) throw new GuideNotAvailableException(guideName);

        Schedule schedule = new Schedule();
        schedule.setGuideId(guideId);
        schedule.setTourName(tourName.trim());
        schedule.setLocationName(locationName.trim());
        schedule.setLatitude(lat);
        schedule.setLongitude(lng);
        schedule.setTourDate(tourDate);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setMaxTourists(Integer.parseInt(maxTourists));
        schedule.setStatus("Upcoming");

        return scheduleDAO.addSchedule(schedule);
    }

    public boolean updateSchedule(int scheduleId, int guideId, String tourName,
                                  String locationName, double lat, double lng,
                                  Date tourDate, Time startTime, Time endTime,
                                  String maxTourists, String status) throws SQLException {

        if (Validator.isNullOrEmpty(tourName)) throw new IllegalArgumentException("Tour name is required.");
        if (Validator.isNullOrEmpty(locationName)) throw new IllegalArgumentException("Location is required.");

        Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleId);
        schedule.setGuideId(guideId);
        schedule.setTourName(tourName.trim());
        schedule.setLocationName(locationName.trim());
        schedule.setLatitude(lat);
        schedule.setLongitude(lng);
        schedule.setTourDate(tourDate);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setMaxTourists(Integer.parseInt(maxTourists));
        schedule.setStatus(status);

        return scheduleDAO.updateSchedule(schedule);
    }

    public boolean deleteSchedule(int scheduleId) throws SQLException {
        return scheduleDAO.deleteSchedule(scheduleId);
    }

    public List<Schedule> getAllSchedules() throws SQLException {
        return scheduleDAO.getAllSchedules();
    }

    public List<Schedule> getUpcomingSchedules() throws SQLException {
        return scheduleDAO.getUpcomingSchedules();
    }

    public Schedule getScheduleById(int id) throws SQLException {
        return scheduleDAO.getScheduleById(id);
    }

    public int getTouristCount(int scheduleId) throws SQLException {
        return scheduleDAO.getTouristCount(scheduleId);
    }

    public int getTotalSchedules() throws SQLException {
        return scheduleDAO.getTotalSchedules();
    }
}