package controller;

import dao.AssignmentDAO;
import dao.ScheduleDAO;
import model.Assignment;
import util.Validator;

import java.sql.SQLException;
import java.util.List;

public class AssignmentController {

    private AssignmentDAO assignmentDAO = new AssignmentDAO();
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    public boolean assignTourist(int scheduleId, int touristId) throws SQLException {

        // Check if already assigned
        if (assignmentDAO.isTouristAssigned(scheduleId, touristId)) {
            throw new IllegalArgumentException("Tourist is already assigned to this tour.");
        }

        // Check if tour is full
        int currentCount = scheduleDAO.getTouristCount(scheduleId);
        int maxTourists = scheduleDAO.getScheduleById(scheduleId).getMaxTourists();
        if (currentCount >= maxTourists) {
            throw new IllegalArgumentException("This tour is already full. Max: " + maxTourists);
        }

        Assignment assignment = new Assignment();
        assignment.setScheduleId(scheduleId);
        assignment.setTouristId(touristId);
        assignment.setStatus("Confirmed");

        return assignmentDAO.addAssignment(assignment);
    }

    public boolean updateStatus(int assignmentId, String status) throws SQLException {
        if (Validator.isNullOrEmpty(status)) throw new IllegalArgumentException("Status is required.");
        return assignmentDAO.updateAssignmentStatus(assignmentId, status);
    }

    public boolean deleteAssignment(int assignmentId) throws SQLException {
        return assignmentDAO.deleteAssignment(assignmentId);
    }

    public List<Assignment> getAllAssignments() throws SQLException {
        return assignmentDAO.getAllAssignments();
    }

    public List<Assignment> getAssignmentsBySchedule(int scheduleId) throws SQLException {
        return assignmentDAO.getAssignmentsBySchedule(scheduleId);
    }

    public List<Assignment> getCompletedWithoutReview() throws SQLException {
        return assignmentDAO.getCompletedWithoutReview();
    }

    public int getTotalAssignments() throws SQLException {
        return assignmentDAO.getTotalAssignments();
    }
}