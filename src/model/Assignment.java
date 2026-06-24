package model;

import java.sql.Timestamp;

public class Assignment {
    private int assignmentId;
    private int scheduleId;
    private int touristId;
    private String touristName;
    private String tourName;
    private Timestamp assignedAt;
    private String status;

    public Assignment() {}

    public Assignment(int assignmentId, int scheduleId, int touristId,
                      String touristName, String tourName,
                      Timestamp assignedAt, String status) {
        this.assignmentId = assignmentId;
        this.scheduleId = scheduleId;
        this.touristId = touristId;
        this.touristName = touristName;
        this.tourName = tourName;
        this.assignedAt = assignedAt;
        this.status = status;
    }

    public int getAssignmentId()    { return assignmentId; }
    public int getScheduleId()      { return scheduleId; }
    public int getTouristId()       { return touristId; }
    public String getTouristName()  { return touristName; }
    public String getTourName()     { return tourName; }
    public Timestamp getAssignedAt(){ return assignedAt; }
    public String getStatus()       { return status; }

    public void setAssignmentId(int assignmentId)       { this.assignmentId = assignmentId; }
    public void setScheduleId(int scheduleId)           { this.scheduleId = scheduleId; }
    public void setTouristId(int touristId)             { this.touristId = touristId; }
    public void setTouristName(String touristName)       { this.touristName = touristName; }
    public void setTourName(String tourName)             { this.tourName = tourName; }
    public void setAssignedAt(Timestamp assignedAt)     { this.assignedAt = assignedAt; }
    public void setStatus(String status)                 { this.status = status; }

    @Override
    public String toString() { return touristName + " → " + tourName; }
}