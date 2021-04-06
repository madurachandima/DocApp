package com.example.docapp.Category_consulter.SingleConsulter;

public class SingleConsulterModel {
    private String userName;
    private String date;
    private String feedback;
    private String userId;


    public SingleConsulterModel() {
    }

    public SingleConsulterModel(String userName, String date, String feedback, String userId) {
        this.userName = userName;
        this.date = date;
        this.feedback = feedback;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
