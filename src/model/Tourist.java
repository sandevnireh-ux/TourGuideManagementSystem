package model;

public class Tourist {
    private int touristId;
    private String fullName;
    private String email;
    private String phone;
    private String nationality;
    private String passportNo;

    public Tourist() {}

    public Tourist(int touristId, String fullName, String email,
                   String phone, String nationality, String passportNo) {
        this.touristId = touristId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.nationality = nationality;
        this.passportNo = passportNo;
    }

    public int getTouristId()       { return touristId; }
    public String getFullName()     { return fullName; }
    public String getEmail()        { return email; }
    public String getPhone()        { return phone; }
    public String getNationality()  { return nationality; }
    public String getPassportNo()   { return passportNo; }

    public void setTouristId(int touristId)         { this.touristId = touristId; }
    public void setFullName(String fullName)         { this.fullName = fullName; }
    public void setEmail(String email)               { this.email = email; }
    public void setPhone(String phone)               { this.phone = phone; }
    public void setNationality(String nationality)   { this.nationality = nationality; }
    public void setPassportNo(String passportNo)     { this.passportNo = passportNo; }

    @Override
    public String toString() { return fullName + " (" + nationality + ")"; }
}