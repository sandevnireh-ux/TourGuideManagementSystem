package model;

import java.sql.Date;
import java.sql.Time;

public class Schedule {
    private int scheduleId;
    private int guideId;
    private String guideName;
    private String tourName;
    private String locationName;
    private double latitude;
    private double longitude;
    private Date tourDate;
    private Time startTime;
    private Time endTime;
    private int maxTourists;
    private String status;

    public Schedule() {}

    public Schedule(int scheduleId, int guideId, String guideName, String tourName,
                    String locationName, double latitude, double longitude,
                    Date tourDate, Time startTime, Time endTime,
                    int maxTourists, String status) {
        this.scheduleId = scheduleId;
        this.guideId = guideId;
        this.guideName = guideName;
        this.tourName = tourName;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tourDate = tourDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxTourists = maxTourists;
        this.status = status;
    }

    public int getScheduleId()      { return scheduleId; }
    public int getGuideId()         { return guideId; }
    public String getGuideName()    { return guideName; }
    public String getTourName()     { return tourName; }
    public String getLocationName() { return locationName; }
    public double getLatitude()     { return latitude; }
    public double getLongitude()    { return longitude; }
    public Date getTourDate()       { return tourDate; }
    public Time getStartTime()      { return startTime; }
    public Time getEndTime()        { return endTime; }
    public int getMaxTourists()     { return maxTourists; }
    public String getStatus()       { return status; }

    public void setScheduleId(int scheduleId)           { this.scheduleId = scheduleId; }
    public void setGuideId(int guideId)                 { this.guideId = guideId; }
    public void setGuideName(String guideName)           { this.guideName = guideName; }
    public void setTourName(String tourName)             { this.tourName = tourName; }
    public void setLocationName(String locationName)     { this.locationName = locationName; }
    public void setLatitude(double latitude)             { this.latitude = latitude; }
    public void setLongitude(double longitude)           { this.longitude = longitude; }
    public void setTourDate(Date tourDate)               { this.tourDate = tourDate; }
    public void setStartTime(Time startTime)             { this.startTime = startTime; }
    public void setEndTime(Time endTime)                 { this.endTime = endTime; }
    public void setMaxTourists(int maxTourists)         { this.maxTourists = maxTourists; }
    public void setStatus(String status)                 { this.status = status; }

    @Override
    public String toString() { return tourName + " - " + locationName; }
}