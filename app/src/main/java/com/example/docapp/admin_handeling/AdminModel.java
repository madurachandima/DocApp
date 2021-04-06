package com.example.docapp.admin_handeling;

public class AdminModel {
    private String conAvailableTime;
    private String conContactNumber;
    private String conDescription;
    private String conLocation;
    private String conName;
    private String experienceYears;
    private String type;
    private String hospitalName;
    private String conEmail;

    public AdminModel() {
    }

    public AdminModel(String conAvailableTime, String conContactNumber, String conDescription,
                      String conLocation, String conName, String experienceYears, String type,
                      String hospitalName, String conEmail) {
        this.conAvailableTime = conAvailableTime;
        this.conContactNumber = conContactNumber;
        this.conDescription = conDescription;
        this.conLocation = conLocation;
        this.conName = conName;
        this.experienceYears = experienceYears;
        this.type = type;
        this.hospitalName = hospitalName;
        this.conEmail = conEmail;
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

    public String getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(String experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getConEmail() {
        return conEmail;
    }

    public void setConEmail(String conEmail) {
        this.conEmail = conEmail;
    }
}
