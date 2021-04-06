package com.example.docapp.Category_consulter;

public class ConsulterModel {
    private String conAvailableTime;
    private String conContactNumber;
    private String conDescription;
    private String conLocation;
    private String conName;
    private String imageURL;
    private String conLongitude;
    private String conLatitude;
    private String experienceYears;
    private String hospitalName;

    private static final String TAG = "ConsulterModel";

    public ConsulterModel() {
    }

    public ConsulterModel(String conAvailableTime, String conContactNumber, String conDescription, String conLocation,
                          String conName, String imageURL, String conLongitude, String conLatitude,
                          String experienceYears, String hospitalName) {
        this.conAvailableTime = conAvailableTime;
        this.conContactNumber = conContactNumber;
        this.conDescription = conDescription;
        this.conLocation = conLocation;
        this.conName = conName;
        this.imageURL = imageURL;
        this.conLongitude = conLongitude;
        this.conLatitude = conLatitude;
        this.experienceYears = experienceYears;
        this.hospitalName = hospitalName;
    }

    public String getConAvailableTime() {
        return conAvailableTime;
    }

    public void setConAvailableTime(String conAvailableTime) {
        this.conAvailableTime = conAvailableTime;
    }

    public String getConContactNumber() {
        return conContactNumber;
    }

    public void setConContactNumber(String conContactNumber) {
        this.conContactNumber = conContactNumber;
    }

    public String getConDescription() {
        return conDescription;
    }

    public void setConDescription(String conDescription) {
        this.conDescription = conDescription;
    }

    public String getConLocation() {
        return conLocation;
    }

    public void setConLocation(String conLocation) {
        this.conLocation = conLocation;
    }

    public String getConName() {
        return conName;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getConLongitude() {
        return conLongitude;
    }

    public void setConLongitude(String conLongitude) {
        this.conLongitude = conLongitude;
    }

    public String getConLatitude() {
        return conLatitude;
    }

    public void setConLatitude(String conLatitude) {
        this.conLatitude = conLatitude;
    }

    public String getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(String experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }


}
