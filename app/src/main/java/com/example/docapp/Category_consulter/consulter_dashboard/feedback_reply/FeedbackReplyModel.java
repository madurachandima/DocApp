package com.example.docapp.Category_consulter.consulter_dashboard.feedback_reply;

public class FeedbackReplyModel {
    private String userName;
    private String date;
    private String feedback;
    private String userId;
    private String reply;
    private String consulterId;
    private String feedbackReply;

    public FeedbackReplyModel() {
    }

    public FeedbackReplyModel(String userName, String date, String feedback, String userId, String reply, String consulterId, String feedbackReply) {
        this.userName = userName;
        this.date = date;
        this.feedback = feedback;
        this.userId = userId;
        this.reply = reply;
        this.consulterId = consulterId;
        this.feedbackReply = feedbackReply;
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

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getConsulterId() {
        return consulterId;
    }

    public void setConsulterId(String consulterId) {
        this.consulterId = consulterId;
    }

    public String getFeedbackReply() {
        return feedbackReply;
    }

    public void setFeedbackReply(String feedbackReply) {
        this.feedbackReply = feedbackReply;
    }
}
