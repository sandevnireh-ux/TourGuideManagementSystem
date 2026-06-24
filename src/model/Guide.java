package model;

public class Guide {
    private int guideId;
    private String fullName;
    private String email;
    private String phone;
    private String language;
    private int experienceYears;
    private String status;
    private double latitude;
    private double longitude;

    public Guide() {}

    public Guide(int guideId, String fullName, String email, String phone,
                 String language, int experienceYears, String status,
                 double latitude, double longitude) {
        this.guideId = guideId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.language = language;
        this.experienceYears = experienceYears;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getGuideId()             { return guideId; }
    public String getFullName()         { return fullName; }
    public String getEmail()            { return email; }
    public String getPhone()            { return phone; }
    public String getLanguage()         { return language; }
    public int getExperienceYears()     { return experienceYears; }
    public String getStatus()           { return status; }
    public double getLatitude()         { return latitude; }
    public double getLongitude()        { return longitude; }

    public void setGuideId(int guideId)             { this.guideId = guideId; }
    public void setFullName(String fullName)         { this.fullName = fullName; }
    public void setEmail(String email)               { this.email = email; }
    public void setPhone(String phone)               { this.phone = phone; }
    public void setLanguage(String language)         { this.language = language; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    public void setStatus(String status)             { this.status = status; }
    public void setLatitude(double latitude)         { this.latitude = latitude; }
    public void setLongitude(double longitude)       { this.longitude = longitude; }

    @Override
    public String toString() { return fullName; }
}
